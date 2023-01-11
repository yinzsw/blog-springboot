package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.List;

/**
 * 用户,角色关联表 通用处理层
 *
 * @author yinzsW
 * @since 23/01/02
 */

public interface UserMtmRoleManager extends IService<UserMtmRolePO> {
    /**
     * 根据用户id获取角色id列表
     *
     * @param userId 用户id
     * @return 角色id列表
     */
    List<Long> listRoleIdsByUserId(Long userId);
}
