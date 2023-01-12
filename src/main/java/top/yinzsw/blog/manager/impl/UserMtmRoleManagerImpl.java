package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yinzsw.blog.manager.UserMtmRoleManager;
import top.yinzsw.blog.mapper.UserMtmRoleMapper;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户,角色关联表 通用处理层实现
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Service
public class UserMtmRoleManagerImpl extends ServiceImpl<UserMtmRoleMapper, UserMtmRolePO> implements UserMtmRoleManager {
    @Override
    public List<Long> listRoleIdsByUserId(Long userId) {
        List<UserMtmRolePO> userMtmRolePOList = lambdaQuery()
                .select(UserMtmRolePO::getRoleId)
                .eq(UserMtmRolePO::getUserId, userId)
                .list();

        return userMtmRolePOList.stream().map(UserMtmRolePO::getRoleId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateUserRoles(Long userId, List<Long> roleIds) {
        lambdaUpdate().eq(UserMtmRolePO::getUserId, userId).remove();
        List<UserMtmRolePO> userMtmRolePOList = roleIds.stream()
                .map(roleId -> new UserMtmRolePO(userId, roleId))
                .collect(Collectors.toList());
        return saveBatch(userMtmRolePOList);
    }
}
