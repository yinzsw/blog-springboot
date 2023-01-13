package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.mapper.CategoryMapper;
import top.yinzsw.blog.model.po.CategoryPO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 文章通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/13
 */
@Service
@RequiredArgsConstructor
public class ArticleManagerImpl implements ArticleManager {
    private final CategoryMapper categoryMapper;

    @Async
    @Override
    public CompletableFuture<Map<Long, String>> getCategoryMappingByCategoryIds(List<Long> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return CompletableFuture.completedFuture(new HashMap<>());
        }

        LambdaQueryWrapper<CategoryPO> queryListWrapper = Wrappers.lambdaQuery(CategoryPO.class)
                .select(CategoryPO::getId, CategoryPO::getCategoryName)
                .in(CategoryPO::getId, categoryIds);
        List<CategoryPO> categoryPOList = categoryMapper.selectList(queryListWrapper);

        Map<Long, String> categoryMapping = categoryPOList.stream().collect(Collectors.toMap(CategoryPO::getId, CategoryPO::getCategoryName));
        return CompletableFuture.completedFuture(categoryMapping);
    }
}
