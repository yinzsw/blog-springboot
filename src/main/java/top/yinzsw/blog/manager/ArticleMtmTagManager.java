package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yinzsW
 * @description 针对表【article_mtm_tag(文章与标签映射表(多对多))】的数据库操作Service
 * @createDate 2023-01-13 09:59:04
 */
public interface ArticleMtmTagManager extends IService<ArticleMtmTagPO> {

    /**
     * 根据文章的标签获取相关文章
     *
     * @param articleId 文章id
     * @return 推荐文章id列表
     */
    List<Long> getRelatedArticleIds(Long articleId);

    /**
     * 根据文章id获取映射表
     *
     * @param articleIds 文章id列表
     * @return 映射表[articleId=tagPO]
     */
    CompletableFuture<Map<Long, List<TagPO>>> getMappingByArticleId(List<Long> articleIds);

    /**
     * 获取标签列表
     *
     * @param articleId 文章id
     * @return 标签列表
     */
    List<TagPO> getTags(Long articleId);

    /**
     * 根据标签id获取文章id列表
     *
     * @param tagId 标签id
     * @return 文章id列表
     */
    List<Long> listArticleIdsByTagId(Long tagId);

    /**
     * 保存没有添加的文章标签
     *
     * @param tagNames  文章标签名
     * @param articleId 文章id
     * @return 是否成功
     */
    boolean saveArticleTagsWileNotExist(List<String> tagNames, Long articleId);

    /**
     * 通过文章id删除 文章与标签的映射关系
     *
     * @param articleIds 文章id列表
     * @return 是否成功
     */
    boolean deleteByArticleId(List<Long> articleIds);
}
