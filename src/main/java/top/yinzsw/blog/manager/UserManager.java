package top.yinzsw.blog.manager;

import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.model.dto.UserLikedDTO;
import top.yinzsw.blog.model.po.UserPO;

import java.time.LocalDateTime;

/**
 * 用户通用业务处理层
 *
 * @author yinzsW
 * @since 22/12/25
 */

public interface UserManager {

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
     * 保存邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void saveEmailVerificationCode(String email, String code);

    /**
     * 校验邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void checkEmailVerificationCode(String email, String code) throws BizException;

    /**
     * 根据用户id查找用户点赞信息
     *
     * @param userId 用户id
     * @return 点赞信息
     */
    UserLikedDTO getUserLikeInfo(Long userId);

    /**
     * 判断文章是否已经被用户点赞过
     *
     * @param uid       用户id
     * @param articleId 文章id
     * @return 状态
     */
    boolean isLikedArticle(Long uid, String articleId);

    /**
     * 保存点赞的文章
     *
     * @param uid       用户id
     * @param articleId 文章id
     */
    void saveLikedArticle(Long uid, String articleId);

    /**
     * 删除点赞过的文章
     *
     * @param uid       用户id
     * @param articleId 文章id
     */
    void deleteLikedArticle(Long uid, String articleId);

/////////////////////////////////////////////////////MYSQL//////////////////////////////////////////////////////////////

    /**
     * 格局用户名或邮箱查询用户
     *
     * @param identity 用户身份标识字符串
     * @return 用户
     */
    UserPO getUserByNameOrEmail(String identity);

    /**
     * 根据用户名或邮箱修改密码
     *
     * @param identity    用户身份标识字符串
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updateUserPassword(String identity, String newPassword);

    /**
     * 异步更新用户信息
     *
     * @param userId        用户id
     * @param userIpAddress 用户ip地址
     * @param lastLoginTime 登录时间
     */
    void updateUserLoginInfo(Long userId, String userIpAddress, LocalDateTime lastLoginTime);
}
