package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.maps.util.MapQueryUtils;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.TagPO;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 文章数据映射模型
 *
 * @author yinzsW
 * @since 23/01/18
 */
@Service
@RequiredArgsConstructor
public class ArticleManagerImpl implements ArticleManager {
    private final StringRedisTemplate stringRedisTemplate;
    private final HttpContext httpContext;

    @Override
    public Map<Long, ArticleHotIndexDTO> getHotIndex(List<Long> articleIds) {
        Object[] ids = articleIds.stream().map(Object::toString).toArray();
        List<Double> likedScore = stringRedisTemplate.opsForZSet().score(ARTICLE_LIKE_COUNT, ids);
        List<Double> viewsScore = stringRedisTemplate.opsForZSet().score(ARTICLE_VIEW_COUNT, ids);
        return IntStream.range(0, ids.length).boxed().collect(Collectors.toMap(articleIds::get, i -> {
            Long liked = Optional.ofNullable(likedScore)
                    .flatMap(scores -> Optional.ofNullable(scores.get(i)))
                    .map(Double::longValue).orElse(0L);
            Long views = Optional.ofNullable(viewsScore)
                    .flatMap(scores -> Optional.ofNullable(scores.get(i)))
                    .map(Double::longValue).orElse(0L);
            return new ArticleHotIndexDTO(liked, views);
        }));
    }

    @Override
    public void updateLikeCount(Long articleId, Long delta) {
        stringRedisTemplate.opsForZSet().incrementScore(ARTICLE_LIKE_COUNT, String.valueOf(articleId), delta);
    }

    @Override
    public void updateViewsCount(Long articleId) {
        Supplier<String> suffixSupplier = () -> {
            try {
                return httpContext.getCurrentContextDTO().getUid().toString();
            } catch (BizException e) {
                return httpContext.getUserIpAddress();
            }
        };
        String antiKey = ARTICLE_VIEW_ANTI_PREFIX + suffixSupplier.get();
        Boolean hasKey = stringRedisTemplate.hasKey(antiKey);
        if (Boolean.FALSE.equals(hasKey)) {
            stringRedisTemplate.opsForValue().set(antiKey, "", Duration.ofHours(1));
            stringRedisTemplate.opsForZSet().incrementScore(ARTICLE_VIEW_COUNT, String.valueOf(articleId), 1L);
        }
    }

    @Override
    public List<Long> listArticleIds(List<Long> tagIds) {
        return MapQueryUtils.create(ArticleMtmTagPO::getTagId, tagIds).getValues(ArticleMtmTagPO::getArticleId);
    }

    @Override
    public List<Long> listRelatedArticleIds(Long articleId) {
        List<Long> tagIds = MapQueryUtils.create(ArticleMtmTagPO::getArticleId, List.of(articleId))
                .getValues(ArticleMtmTagPO::getTagId);
        return Db.lambdaQuery(ArticleMtmTagPO.class)
                .ne(ArticleMtmTagPO::getArticleId, articleId)
                .in(ArticleMtmTagPO::getTagId, tagIds)
                .list().stream()
                .map(ArticleMtmTagPO::getArticleId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public CategoryPO saveCategory(String categoryName) {
        return Db.lambdaQuery(CategoryPO.class)
                .eq(CategoryPO::getCategoryName, categoryName)
                .oneOpt()
                .orElseGet(() -> {
                    CategoryPO categoryPO = new CategoryPO().setCategoryName(categoryName);
                    Db.save(categoryPO);
                    return categoryPO;
                });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveTagsAndMapping(List<String> tagNames, Long articleId) {
        //获取未保存的标签名
        List<TagPO> existTagPOList = Db.lambdaQuery(TagPO.class).in(TagPO::getTagName, tagNames).list();
        List<TagPO> newTagPOList = tagNames.stream()
                .filter(tagName -> existTagPOList.stream().map(TagPO::getTagName).noneMatch(tagName::equalsIgnoreCase))
                .map(tageName -> new TagPO().setTagName(tageName))
                .collect(Collectors.toList());

        //保存新的标签名
        if (!CollectionUtils.isEmpty(newTagPOList)) {
            Db.saveBatch(newTagPOList);
        }

        //更新标签与文章映射关系
        deleteTagsMapping(List.of(articleId));
        List<Long> existTagIds = existTagPOList.stream().map(TagPO::getId).collect(Collectors.toList());
        List<ArticleMtmTagPO> articleMtmTagPOList = newTagPOList.stream()
                .map(TagPO::getId).collect(Collectors.toCollection(() -> existTagIds)).stream()
                .map(tagId -> new ArticleMtmTagPO().setArticleId(articleId).setTagId(tagId)).collect(Collectors.toList());
        return Db.saveBatch(articleMtmTagPOList);
    }

    @Override
    public void deleteTagsMapping(List<Long> articleIds) {
        Db.lambdaUpdate(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getArticleId, articleIds).remove();
    }
}
