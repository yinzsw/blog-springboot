package top.yinzsw.blog.core.maps.mapping;

import top.yinzsw.blog.core.maps.handler.MapTaskRunnerTemplate;
import top.yinzsw.blog.core.maps.util.MapQueryUtils;
import top.yinzsw.blog.model.dto.CategoryMapsDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 分类映射
 *
 * @author yinzsW
 * @since 23/01/27
 */

public final class CategoryMapping extends MapTaskRunnerTemplate<CategoryPO, CategoryMapsDTO> {
    public CategoryMapping(Executor executor, List<CategoryPO> originList) {
        super(executor, originList, new CategoryMapsDTO());
    }

    private List<Long> getCategoryIds() {
        return getOriginList().stream().map(CategoryPO::getId).collect(Collectors.toList());
    }

    public CategoryMapping mapArticleCount() {
        addRunnable(() -> {
            List<Long> categoryIds = getCategoryIds();
            Map<Long, Long> article2CountMap = MapQueryUtils.create(ArticlePO::getCategoryId, categoryIds)
                    .queryWrapper(wrapper -> wrapper.groupBy(ArticlePO::getCategoryId))
                    .getKeyValueMap(ArticlePO::getArticleCount);
            getContextDTO().setMapArticleCount(article2CountMap);
        });
        return this;
    }
}
