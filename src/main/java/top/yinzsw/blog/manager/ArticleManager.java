package top.yinzsw.blog.manager;

import top.yinzsw.blog.model.po.CategoryPO;

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
    CompletableFuture<Map<Long, String>> getCategoryMappingByCategoryId(List<Long> categoryIds);

    /**
     * 获取分类信息
     *
     * @param categoryId 分类id
     * @return 分类信息
     */
    CategoryPO getCategory(Long categoryId);

    /**
     * 取消多余的置顶文章
     */
    void cancelOverTopArticle();

    /**
     * 保存没有添加的文章分类
     *
     * @param categoryName 文章分类名
     * @return 文章分类对象
     */
    CategoryPO saveArticleCategoryWileNotExist(String categoryName);
}
