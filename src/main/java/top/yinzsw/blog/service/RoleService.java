package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.RolePO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Service
 * @createDate 2022-12-15 14:47:44
 */
public interface RoleService extends IService<RolePO> {

    /**
     * 根据用户id获取用户的角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<String> getRoleNamesByUserId(Long userId);

    /**
     * 根据资源id获取角色列表
     *
     * @param resourceId 资源id
     * @return 角色列表
     */
    List<String> getRoleNamesByResourceId(Long resourceId);
}
