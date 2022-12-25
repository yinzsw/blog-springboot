package top.yinzsw.blog.constant;

/**
 * redis常量
 *
 * @author yinzsW
 * @since 22/12/15
 */
public class RedisConst {

    /**
     * 用户点赞过的文章
     */
    public static final String USER_LIKED_ARTICLES_PREFIX = "blog:user:liked_articles:";

    /**
     * 用户点赞过的评论
     */
    public static final String USER_LIKED_COMMENTS_PREFIX = "blog:user:liked_comments:";

    /**
     * 用户点赞过的说说
     */
    public static final String USER_LIKED_TALKS_PREFIX = "blog:user:liked_talks:";

    /**
     * 文章点击量
     */
    public static final String ARTICLE_VIEWS_COUNT = "blog:article:hits";

    /**
     * 文章点赞量
     */
    public static final String ARTICLE_LIKE_COUNT = "blog:article:likes";

    /**
     * 网站配置
     */
    public static final String WEBSITE_CONFIG = "blog:website:config";
}
