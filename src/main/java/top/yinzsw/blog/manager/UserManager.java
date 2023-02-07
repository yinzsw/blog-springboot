package top.yinzsw.blog.manager;

import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.UserPO;

import java.time.Duration;

/**
 * 用户通用业务处理层
 *
 * @author yinzsW
 * @since 22/12/25
 */

public interface UserManager extends CommonManager<UserPO> {

    /**
     * 邮箱验证码过期时间(分钟)
     */
    Duration USER_EMAIL_CODE_EXPIRE_TIME = Duration.ofMinutes(15);

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
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 验证码
     */
    String sendEmailCode(String email);

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
     * 保存用户登录历史
     *
     * @param userId    用户id
     * @param userAgent 用户代理字符串
     * @param ipAddress ip地址
     */
    void saveUserLoginHistory(Long userId, String userAgent, String ipAddress);
}
