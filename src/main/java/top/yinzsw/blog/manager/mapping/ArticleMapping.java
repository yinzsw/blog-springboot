package top.yinzsw.blog.manager.mapping;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.maps.AbstractMapsBuilder;
import top.yinzsw.blog.core.maps.SimpleMapProvider;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.TagPO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章映射
 *
 * @author yinzsW
 * @since 23/01/19
 */
@Service
@RequiredArgsConstructor
public class ArticleMapping {
    private final ArticleManager articleManager;
    private final ThreadPoolTaskExecutor taskExecutor;

    public Builder builder(List<ArticlePO> articlePOList) {
        return new Builder(articlePOList, new ArticleMapsDTO());
    }

    public final List<Long> listArticleIds(Long... tagIds) {
        return new SimpleMapProvider<>(ArticleMtmTagPO::getTagId, List.of(tagIds))
                .getValues(ArticleMtmTagPO::getArticleId);
    }

    public final List<Long> listRelatedArticleIds(Long articleId) {
        List<Long> tagIds = new SimpleMapProvider<>(ArticleMtmTagPO::getArticleId, List.of(articleId))
                .getValues(ArticleMtmTagPO::getTagId);
        return Db.lambdaQuery(ArticleMtmTagPO.class)
                .ne(ArticleMtmTagPO::getArticleId, articleId)
                .in(ArticleMtmTagPO::getTagId, tagIds)
                .list().stream()
                .map(ArticleMtmTagPO::getArticleId)
                .distinct()
                .collect(Collectors.toList());
    }

    public final CategoryPO saveCategory(String categoryName) {
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
                .map(tagId -> new ArticleMtmTagPO(articleId, tagId)).collect(Collectors.toList());
        return Db.saveBatch(articleMtmTagPOList);
    }

    public void deleteTagsMapping(List<Long> articleIds) {
        Db.lambdaUpdate(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getArticleId, articleIds).remove();
    }


    public class Builder extends AbstractMapsBuilder<ArticlePO, ArticleMapsDTO> {
        protected Builder(List<ArticlePO> originList, ArticleMapsDTO articleMapsDTO) {
            super(originList, articleMapsDTO);
        }

        @Override
        protected Executor getExecutor() {
            return taskExecutor;
        }

        public final Builder mapCategory(Long... categoryIds) {
            addRunnable(() -> {
                List<Long> categoryIdList = getIds(ArticlePO::getCategoryId, categoryIds);
                context.setCategoryNameMap(new SimpleMapProvider<>(CategoryPO::getId, categoryIdList)
                        .getKeyValueMap(CategoryPO::getCategoryName));
            });
            return this;
        }

        public final Builder mapTags(Long... articleIds) {
            addRunnable(() -> {
                List<Long> articleIdList = getIds(ArticlePO::getId, articleIds);
                Map<Long, List<Long>> articleIdMapTagIds = new SimpleMapProvider<>(ArticleMtmTagPO::getArticleId, articleIdList)
                        .getGroupValueMap(ArticleMtmTagPO::getTagId);

                List<Long> distinctTagIds = articleIdMapTagIds.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
                Map<Long, TagPO> tagIdMapTag = new SimpleMapProvider<>(TagPO::getId, distinctTagIds).getKeyMap(TagPO::getId, TagPO::getTagName);

                context.setTagsMap(articleIdMapTagIds.keySet().stream().collect(Collectors.toMap(Function.identity(), articleId -> {
                    List<Long> tagIds = articleIdMapTagIds.get(articleId);
                    return tagIds.stream().map(tagIdMapTag::get).collect(Collectors.toList());
                })));
            });
            return this;
        }

        public final Builder mapHotIndex(Long... articleIds) {
            addRunnable(() -> {
                context.setLikeCountMap(articleManager.getLikesCountMap(articleIds));
                context.setViewCountMap(articleManager.getViewsCountMap(articleIds));
            });
            return this;
        }
    }
}
