package top.yinzsw.blog.manager;

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
     * 获取文章浏览量信息
     *
     * @param articleIds 文章id列表
     * @return 文章点赞信息[文章id=浏览量]
     */
    Map<Long, Long> getViewsCountMap(Long... articleIds);

    /**
     * 获取文章点赞量信息
     *
     * @param articleIds 文章id列表
     * @return 文章点赞信息[文章id=点赞量]
     */
    Map<Long, Long> getLikesCountMap(Long... articleIds);

    /**
     * 修改文章浏览量
     *
     * @param articleId 文章id
     */
    void updateViewsCount(Long articleId);

    /**
     * 修改文章点赞数
     *
     * @param articleId 文章id
     * @param delta     递增值
     */
    void updateLikeCount(Long articleId, Long delta);
}
