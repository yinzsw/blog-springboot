package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.util.MybatisPlusUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 文章通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/13
 */
@Service
@RequiredArgsConstructor
public class ArticleManagerImpl implements ArticleManager {
    @Async
    @Override
    public CompletableFuture<Map<Long, String>> getCategoryMappingByCategoryId(List<Long> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return CompletableFuture.completedFuture(Collections.emptyMap());
        }

        Map<Long, String> categoryMapping = MybatisPlusUtils.mappingMap(CategoryPO::getId, CategoryPO::getCategoryName, categoryIds);
        return CompletableFuture.completedFuture(categoryMapping);
    }

    @Override
    public CategoryPO saveArticleCategoryWileNotExist(String categoryName) {
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
