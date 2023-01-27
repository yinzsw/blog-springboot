package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.model.po.ArticlePO;

import java.util.List;

/**
 * 分类通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Service
public class CategoryManagerImpl implements CategoryManager {
    @Override
    public boolean hasUseArticle(List<Long> categoryIds) {
        return Db.lambdaQuery(ArticlePO.class).in(ArticlePO::getCategoryId, categoryIds).count() > 0L;
    }
}
