package top.yinzsw.blog.core.maps;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.maps.mapping.ArticleMapping;
import top.yinzsw.blog.core.maps.mapping.RoleMapping;
import top.yinzsw.blog.core.maps.mapping.TagMapping;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.TagPO;

import java.util.List;

/**
 * 映射工厂
 *
 * @author yinzsW
 * @since 23/01/26
 */
@Service
@RequiredArgsConstructor
public class MappingFactory {
    private final ThreadPoolTaskExecutor taskExecutor;

    public ArticleMapping getArticleMapping(List<ArticlePO> articlePOList) {
        return new ArticleMapping(taskExecutor, articlePOList);
    }

    public RoleMapping getRoleMapping(List<RolePO> rolePOList) {
        return new RoleMapping(taskExecutor, rolePOList);
    }

    public TagMapping getTagMapping(List<TagPO> tagPOList) {
        return new TagMapping(taskExecutor, tagPOList);
    }
}
