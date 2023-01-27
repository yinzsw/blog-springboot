package top.yinzsw.blog.manager;

import java.util.List;

/**
 * 分类通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/27
 */

public interface CategoryManager {
    /**
     * 判断此分类是否被使用
     *
     * @param categoryIds 分类id
     * @return 是否被使用
     */
    boolean hasUseArticle(List<Long> categoryIds);
}
