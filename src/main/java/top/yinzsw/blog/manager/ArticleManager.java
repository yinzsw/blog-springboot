package top.yinzsw.blog.manager;

import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.po.CategoryPO;

import java.util.List;
import java.util.Map;

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
     * 获取文章点赞量信息 与 文章浏览量信息
     *
     * @param articleIds 文章id列表
     * @return 热度信息(点赞量, 浏览量)
     */
    Map<Long, ArticleHotIndexDTO> getHotIndex(List<Long> articleIds);

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

    /////////////////////////////////////////////////////////////MYSQL//////////////////////////////////////////////////////

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
    List<Long> listRelatedArticleIds(Long articleId);

    /**
     * 保存分类
     *
     * @param categoryName 分类名
     * @return 文章分类信息
     */
    CategoryPO saveCategory(String categoryName);

    /**
     * 保存标签
     *
     * @param tagNames  标签名
     * @param articleId 文章id
     * @return 是否成功
     */
    boolean saveTagsAndMapping(List<String> tagNames, Long articleId);

    /**
     * 删除文章标签
     *
     * @param articleIds 文章id列表
     */
    void deleteTagsMapping(List<Long> articleIds);
}
