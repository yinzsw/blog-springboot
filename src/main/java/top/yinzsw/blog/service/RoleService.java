package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.model.vo.UserRoleVO;

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

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    List<UserRoleVO> listUserRoles();

    /**
     * 根据用户名关键词 分页获取所有用户角色信息
     *
     * @param pageReq  分页信息
     * @param keywords 用户名关键词
     * @return 用户角色列表信息
     */
    PageVO<RoleVO> pageListRoles(PageReq pageReq, String keywords);
}
