package top.yinzsw.blog.manager;

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
     */
    void asyncUpdateUserLoginInfo(Long userId, String userIpAddress);
}
