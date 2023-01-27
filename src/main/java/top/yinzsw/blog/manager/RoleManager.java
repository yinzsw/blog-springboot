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
     * 根据用户id查询角色id
     *
     * @param userId 用户id
     * @return 角色id列表
     */
    List<Long> listRoleIdsByUserId(Long userId);

    /**
     * 根据资源id查找角色id
     *
     * @param resourceId 资源id
     * @return 角色id列表
     */
    List<Long> listRoleIdsByResourceId(Long resourceId);

    /**
     * 获取未禁用的角色名列表
     *
     * @param roleIds 角色id
     * @return 角色名列表
     */
    List<String> getEnabledRoleNamesByIds(List<Long> roleIds);

    /**
     * 查询角色是否正在被用户使用
     *
     * @param roleIds 角色id列表
     * @return 是否被用户使用
     */
    boolean hasUseUser(List<Long> roleIds);

    /**
     * 保存菜单映射
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    void saveMenusMapping(Long roleId, List<Long> menuIds);

    /**
     * 保存资源映射
     *
     * @param roleId      角色id列表
     * @param resourceIds 资源id列表
     */
    void saveResourcesMapping(Long roleId, List<Long> resourceIds);
}
