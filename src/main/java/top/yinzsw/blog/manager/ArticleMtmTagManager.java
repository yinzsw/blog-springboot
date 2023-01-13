package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;

import java.util.List;
import java.util.Map;

/**
 * @author yinzsW
 * @description 针对表【article_mtm_tag(文章与标签映射表(多对多))】的数据库操作Service
 * @createDate 2023-01-13 09:59:04
 */
public interface ArticleMtmTagManager extends IService<ArticleMtmTagPO> {

    /**
     * 根据文章id获取映射表
     *
     * @param articleIds 文章id列表
     * @return 映射表[articleId=tagPO]
     */
    Map<Long, List<TagPO>> getMappingByArticleIds(List<Long> articleIds);
}
