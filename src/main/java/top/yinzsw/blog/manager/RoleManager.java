package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.dto.RoleMapsDTO;
import top.yinzsw.blog.model.po.RolePO;

import java.util.List;

/**
 * 角色通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/25
 */

public interface RoleManager extends CommonManager<RolePO> {

    /**
     * 获取未禁用的角色名列表
     *
     * @param roleIds 角色id
     * @return 角色名列表
     */
    List<String> getEnabledRoleNamesByIds(List<Long> roleIds);

    ///////////////////////////////////////////////////MapsContext//////////////////////////////////////////////////////


    /**
     * 根据角色id获取菜单id和资源id信息
     *
     * @param roleIds 角色id列表
     * @return 映射信息
     */
    RoleMapsDTO getRoleMapsDTO(List<Long> roleIds);
}
