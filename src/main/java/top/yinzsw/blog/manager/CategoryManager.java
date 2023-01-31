package top.yinzsw.blog.manager;

import top.yinzsw.blog.model.po.CategoryPO;

/**
 * 分类通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/27
 */

public interface CategoryManager {


    /**
     * 保存分类
     *
     * @param categoryName 分类名
     * @return 文章分类信息
     */
    CategoryPO saveCategory(String categoryName);
}
