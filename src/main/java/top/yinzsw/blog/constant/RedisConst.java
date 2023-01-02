package top.yinzsw.blog.constant;

/**
 * redis常量
 *
 * @author yinzsW
 * @since 22/12/15
 */
public final class RedisConst {
    /**
     * 缓存
     */
    public static final String CACHE_PREFIX = "blog:cache:";

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
     * 邮箱验证码
     */
    public static final String USER_EMAIL_CODE_PREFIX = "blog:user:email_code:";

    /**
     * 邮箱验证码过期时间(分钟)
     */
    public static final int USER_EMAIL_CODE_EXPIRE_TIME = 15;

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
