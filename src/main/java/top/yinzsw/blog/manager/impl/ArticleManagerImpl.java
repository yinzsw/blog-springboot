package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.maps.DataMapBuilder;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.MapQueryUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
    private final HttpContext httpContext;
    private final DataMapBuilder dataMapBuilder;
    private final StringRedisTemplate stringRedisTemplate;


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
    public List<Long> listRelatedArticleIdsLimit6(Long articleId) {
        List<Long> tagIds = MapQueryUtils.create(ArticleMtmTagPO::getArticleId, List.of(articleId))
                .getValues(ArticleMtmTagPO::getTagId);

        List<ArticleMtmTagPO> articleMtmTagPOList = Db.lambdaQuery(ArticleMtmTagPO.class)
                .select(ArticleMtmTagPO::getArticleId)
                .ne(ArticleMtmTagPO::getArticleId, articleId)
                .in(ArticleMtmTagPO::getTagId, tagIds)
                .list();

        return articleMtmTagPOList.stream()
                .map(ArticleMtmTagPO::getArticleId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveTagsMapping(Long articleId, List<String> tagNames) {
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
        Db.lambdaUpdate(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getArticleId, List.of(articleId)).remove();
        List<Long> existTagIds = existTagPOList.stream().map(TagPO::getId).collect(Collectors.toList());
        List<ArticleMtmTagPO> articleMtmTagPOList = newTagPOList.stream()
                .map(TagPO::getId).collect(Collectors.toCollection(() -> existTagIds)).stream()
                .map(tagId -> new ArticleMtmTagPO().setArticleId(articleId).setTagId(tagId)).collect(Collectors.toList());
        return Db.saveBatch(articleMtmTagPOList);
    }

    @Override
    public ArticleMapsDTO getArticleMapsDTO(List<ArticlePO> articlePOList, boolean mapHotIndex) {
        List<Long> articleIds = CommonUtils.toList(articlePOList, ArticlePO::getId);
        List<Long> categoryIds = CommonUtils.toList(articlePOList, ArticlePO::getCategoryId);

        DataMapBuilder.Builder<ArticleMapsDTO> builder = dataMapBuilder.builder(new ArticleMapsDTO());
        builder.addMap(ArticleMapsDTO::setCategoryNameMap, () -> getCategoryNameMap(categoryIds));
        builder.addMap(ArticleMapsDTO::setTagsMap, () -> getTagsMap(articleIds));
        if (mapHotIndex) {
            builder.addMap(ArticleMapsDTO::setHotIndexMap, () -> getHotIndex(articleIds));
        }
        return builder.build();
    }

    private Map<Long, String> getCategoryNameMap(List<Long> categoryIds) {
        return MapQueryUtils.create(CategoryPO::getId, categoryIds).getKeyValueMap(CategoryPO::getCategoryName);
    }

    private Map<Long, List<TagPO>> getTagsMap(List<Long> articleIds) {
        Map<Long, List<Long>> articleId2TagIds = MapQueryUtils.create(ArticleMtmTagPO::getArticleId, articleIds)
                .getGroupValueMap(ArticleMtmTagPO::getTagId);

        List<Long> distinctTagIds = articleId2TagIds.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
        Map<Long, TagPO> tagId2Tag = MapQueryUtils.create(TagPO::getId, distinctTagIds)
                .queryWrapper(q -> q.select(TagPO::getId, TagPO::getTagName))
                .getKeyMap();

        return articleId2TagIds.keySet().stream().collect(Collectors.toMap(Function.identity(), articleId -> {
            List<Long> tagIds = articleId2TagIds.get(articleId);
            return tagIds.stream().map(tagId2Tag::get).collect(Collectors.toList());
        }));
    }

    private Map<Long, ArticleHotIndexDTO> getHotIndex(List<Long> articleIds) {
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
}
