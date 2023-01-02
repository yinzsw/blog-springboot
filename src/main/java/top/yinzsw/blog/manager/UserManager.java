package top.yinzsw.blog.manager;

import top.yinzsw.blog.exception.BizException;

import java.time.LocalDateTime;

/**
 * 用户通用业务处理层
 *
 * @author yinzsW
 * @since 22/12/25
 */

public interface UserManager {

    /**
     * 异步更新用户信息
     *
     * @param userId        用户id
     * @param userIpAddress 用户ip地址
     * @param lastLoginTime 登录时间
     */
    void asyncUpdateUserLoginInfo(Long userId, String userIpAddress, LocalDateTime lastLoginTime);

    /**
     * 校验邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void checkEmailVerificationCode(String email, String code) throws BizException;
}
