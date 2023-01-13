package top.yinzsw.blog.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 文章通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/13
 */

public interface ArticleManager {
    /**
     * 根据分类id获取映射表
     *
     * @param categoryIds 分类id
     * @return 映射表[categoryId=categoryName]
     */
    CompletableFuture<Map<Long, String>> getCategoryMappingByCategoryIds(List<Long> categoryIds);
}
