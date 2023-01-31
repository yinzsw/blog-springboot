package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.model.po.CategoryPO;

/**
 * 分类通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Service
public class CategoryManagerImpl implements CategoryManager {
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
}
