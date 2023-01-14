package top.yinzsw.blog.manager;

import top.yinzsw.blog.exception.BizException;
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

    /**
     * 校验邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void checkEmailVerificationCode(String email, String code) throws BizException;
}
