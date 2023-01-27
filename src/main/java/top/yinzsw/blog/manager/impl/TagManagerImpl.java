package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.TagManager;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;

import java.util.List;

/**
 * 标签通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/27
 */

@Service
public class TagManagerImpl implements TagManager {
    @Override
    public boolean hasUseArticle(List<Long> tagIds) {
        return Db.lambdaQuery(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getTagId, tagIds).count() > 0L;
    }
}
