package top.yinzsw.blog.manager;

import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.po.ArticlePO;

import java.util.List;

/**
 * 文章通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/13
 */

public interface ArticleManager {

    /**
     * 文章点击量
     */
    String ARTICLE_VIEW_COUNT = "blog:article:views";

    /**
     * 文章点击量 防刷保护
     */
    String ARTICLE_VIEW_ANTI_PREFIX = "blog:article:views:anti:";

    /**
     * 文章点赞量
     */
    String ARTICLE_LIKE_COUNT = "blog:article:likes";

    /**
     * 修改文章点赞数
     *
     * @param articleId 文章id
     * @param delta     递增值
     */
    void updateLikeCount(Long articleId, Long delta);

    /**
     * 修改文章浏览量
     *
     * @param articleId 文章id
     */
    void updateViewsCount(Long articleId);

/////////////////////////////////////////////////////////MYSQL//////////////////////////////////////////////////////////

    /**
     * 根据标签查询文章id
     *
     * @param tagIds 标签id
     * @return 文章id列表
     */
    List<Long> listArticleIds(List<Long> tagIds);

    /**
     * 查询相关文章id列表
     *
     * @param articleId 文章id
     * @return 相关文章id
     */
    List<Long> listRelatedArticleIdsLimit6(Long articleId);

    /**
     * 保存标签
     *
     * @param articleId 文章id
     * @param tagNames  标签名
     * @return 是否成功
     */
    boolean saveTagsMapping(Long articleId, List<String> tagNames);


    ///////////////////////////////////////////////////MapsContext//////////////////////////////////////////////////////

    /**
     * 获取文章分类信息,标签信息,热度信息
     *
     * @param articlePOList 文章原始对象列表
     * @param mapHotIndex   是否映射热度信息
     * @return 映射上下文模型
     */
    ArticleMapsDTO getArticleMapsDTO(List<ArticlePO> articlePOList, boolean mapHotIndex);
}
