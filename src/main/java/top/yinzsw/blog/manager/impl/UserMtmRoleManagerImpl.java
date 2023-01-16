package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yinzsw.blog.manager.UserMtmRoleManager;
import top.yinzsw.blog.mapper.UserMtmRoleMapper;
import top.yinzsw.blog.model.po.UserMtmRolePO;
import top.yinzsw.blog.util.MybatisPlusUtils;

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
        return MybatisPlusUtils.mappingList(UserMtmRolePO::getUserId, UserMtmRolePO::getRoleId, userId);
    }

    @Override
    public List<Long> listUserIdsByRoleId(List<Long> roleIdList) {
        return MybatisPlusUtils.mappingDistinctList(UserMtmRolePO::getRoleId, UserMtmRolePO::getUserId, roleIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserRoles(Long userId, List<Long> roleIds) {
        lambdaUpdate().eq(UserMtmRolePO::getUserId, userId).remove();
        List<UserMtmRolePO> userMtmRolePOList = roleIds.stream()
                .map(roleId -> new UserMtmRolePO(userId, roleId))
                .collect(Collectors.toList());
        // TODO 尝试更新用户token
        return saveBatch(userMtmRolePOList);
    }
}
