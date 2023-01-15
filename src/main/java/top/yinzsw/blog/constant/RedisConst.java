package top.yinzsw.blog.constant;

import top.yinzsw.blog.exception.DaoException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * redis常量
 *
 * @author yinzsW
 * @since 22/12/15
 */
public interface RedisConst {

    /**
     * 界定符
     */
    String DELIMITER = ":";

    /**
     * 邮箱验证码过期时间(分钟)
     */
    int USER_EMAIL_CODE_EXPIRE_TIME = 15;

    /**
     * 邮箱验证码
     */
    String USER_EMAIL_CODE_PREFIX = "blog:user:email_code:";

    /**
     * 用户点赞过的说说
     */
    String USER_LIKED_TALKS_PREFIX = "blog:user:liked_talks:";

    /**
     * 用户点赞过的文章
     */
    String USER_LIKED_ARTICLES_PREFIX = "blog:user:liked_articles:";

    /**
     * 用户点赞过的评论
     */
    String USER_LIKED_COMMENTS_PREFIX = "blog:user:liked_comments:";

    /**
     * 文章点击量
     */
    String ARTICLE_VIEW_COUNT = "blog:article:views";

    /**
     * 文章点赞量
     */
    String ARTICLE_LIKE_COUNT = "blog:article:likes";

    /**
     * 网站配置
     */
    String WEBSITE_CONFIG = "blog:website:config";


    /**
     * 创建Redis Key
     *
     * @param prefix 前缀
     * @param params 参数
     * @return Redis KEY
     */
    default String createKey(String prefix, Object... params) {
        if (Objects.isNull(prefix) || params.length == 0) {
            throw new DaoException("不合法的Redis KEY");
        }
        String suffix = Arrays.stream(params)
                .map(Object::toString)
                .collect(Collectors.joining(DELIMITER));
        return prefix.concat(suffix);
    }
}
