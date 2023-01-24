package top.yinzsw.blog.manager;

import java.util.List;

/**
 * 角色通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/25
 */

public interface RoleManager {

    /**
     * 获取未禁用的角色名列表
     *
     * @param roleIds 角色id
     * @return 角色名列表
     */
    List<String> getEnabledRoleNamesByIds(List<Long> roleIds);
}
