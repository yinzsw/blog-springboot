package top.yinzsw.blog.manager;

import java.util.List;
import java.util.Map;

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
    Map<Long, String> getCategoryMappingByCategoryIds(List<Long> categoryIds);
}
