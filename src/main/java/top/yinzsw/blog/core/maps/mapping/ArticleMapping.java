package top.yinzsw.blog.core.maps.mapping;

import top.yinzsw.blog.core.maps.handler.MapTaskRunnerTemplate;
import top.yinzsw.blog.core.maps.util.MapQueryUtils;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
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
 * @since 23/01/26
 */

public final class ArticleMapping extends MapTaskRunnerTemplate<ArticlePO, ArticleMapsDTO> {
    public ArticleMapping(Executor executor, List<ArticlePO> articlePOList) {
        super(executor, articlePOList, new ArticleMapsDTO());
    }

    private List<Long> getArticleIds() {
        return getOriginList().stream().map(ArticlePO::getId).collect(Collectors.toList());
    }

    private List<Long> getCategoryIds() {
        return getOriginList().stream().map(ArticlePO::getCategoryId).collect(Collectors.toList());
    }

    public ArticleMapping mapCategory() {
        addRunnable(() -> {
            Map<Long, String> categoryNameMap = MapQueryUtils.create(CategoryPO::getId, getCategoryIds())
                    .getKeyValueMap(CategoryPO::getCategoryName);

            getContextDTO().setCategoryNameMap(categoryNameMap);
        });
        return this;
    }

    public ArticleMapping mapTags() {
        addRunnable(() -> {
            Map<Long, List<Long>> articleId2TagIds = MapQueryUtils.create(ArticleMtmTagPO::getArticleId, getArticleIds())
                    .getGroupValueMap(ArticleMtmTagPO::getTagId);

            List<Long> distinctTagIds = articleId2TagIds.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
            Map<Long, TagPO> tagId2Tag = MapQueryUtils.create(TagPO::getId, distinctTagIds)
                    .queryWrapper(q -> q.select(TagPO::getId, TagPO::getTagName))
                    .getKeyMap();

            getContextDTO().setTagsMap(articleId2TagIds.keySet().stream()
                    .collect(Collectors.toMap(Function.identity(), articleId -> {
                        List<Long> tagIds = articleId2TagIds.get(articleId);
                        return tagIds.stream().map(tagId2Tag::get).collect(Collectors.toList());
                    })));
        });
        return this;
    }

    public ArticleMapping mapHotIndex(Function</*articleIds*/List<Long>, Map<Long, ArticleHotIndexDTO>> mapFunction) {
        addRunnable(() -> getContextDTO().setHotIndexMap(mapFunction.apply(getArticleIds())));
        return this;
    }
}
