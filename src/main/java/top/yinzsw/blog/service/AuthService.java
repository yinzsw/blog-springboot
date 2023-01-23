package top.yinzsw.blog.service;

import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;

/**
 * 用户认证业务接口
 *
 * @author yinzsW
 * @since 22/12/21
 */

public interface AuthService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    UserInfoVO login(String username, String password);

    /**
     * 刷新用户token
     *
     * @return token信息
     */
    TokenVO refreshToken();

    /**
     * 退出登录
     *
     * @return 是否成功
     */
    boolean logout();

    /**
     * 发送验证邮件
     *
     * @param email 邮箱
     * @return 是否成功
     */
    boolean sendEmailCode(String email);
}
