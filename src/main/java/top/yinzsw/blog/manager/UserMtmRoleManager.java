package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.service.IMappingService;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.List;

/**
 * 用户,角色关联表 通用处理层
 *
 * @author yinzsW
 * @since 23/01/02
 */

public interface UserMtmRoleManager extends IMappingService<UserMtmRolePO> {
    /**
     * 根据用户id获取角色id列表
     *
     * @param userId 用户id
     * @return 角色id列表
     */
    List<Long> listRoleIdsByUserId(Long userId);

    /**
     * 根据角色id列表获取用户数量
     *
     * @param roleIdList 角色id列表
     * @return 用户数量
     */
    Long countUserByRoleId(List<Long> roleIdList);

    /**
     * 根据id更新用户角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     * @return 是否成功
     */
    Boolean updateUserRoles(Long userId, List<Long> roleIds);
}
