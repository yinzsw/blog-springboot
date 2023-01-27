package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.maps.util.MapQueryUtils;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/25
 */
@Service
@RequiredArgsConstructor
public class RoleManagerImpl implements RoleManager {
    @Override
    public List<Long> listRoleIdsByUserId(Long userId) {
        return MapQueryUtils.create(UserMtmRolePO::getUserId, List.of(userId)).getValues(UserMtmRolePO::getRoleId);
    }

    @Override
    public List<Long> listRoleIdsByResourceId(Long resourceId) {
        return MapQueryUtils.create(RoleMtmResourcePO::getResourceId, List.of(resourceId)).getValues(RoleMtmResourcePO::getRoleId);
    }

    @Override
    public List<String> getEnabledRoleNamesByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        return MapQueryUtils.create(RolePO::getId, roleIds)
                .queryWrapper(q -> q.eq(RolePO::getIsDisabled, false))
                .getValues(RolePO::getRoleLabel);
    }

    @Override
    public boolean hasUseUser(List<Long> roleIds) {
        return Db.lambdaQuery(UserMtmRolePO.class).in(UserMtmRolePO::getRoleId, roleIds).count() > 0L;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMenusMapping(Long roleId, List<Long> menuIds) {
        Db.lambdaUpdate(RoleMtmMenuPO.class).eq(RoleMtmMenuPO::getRoleId, roleId).remove();

        if (!CollectionUtils.isEmpty(menuIds)) {
            List<RoleMtmMenuPO> roleMtmMenuPOList = menuIds.stream()
                    .map(menuId -> new RoleMtmMenuPO(roleId, menuId))
                    .collect(Collectors.toList());
            Db.saveBatch(roleMtmMenuPOList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveResourcesMapping(Long roleId, List<Long> resourceIds) {
        Db.lambdaUpdate(RoleMtmResourcePO.class).eq(RoleMtmResourcePO::getRoleId, roleId).remove();

        if (!CollectionUtils.isEmpty(resourceIds)) {
            List<RoleMtmResourcePO> roleMtmResourcePOList = resourceIds.stream()
                    .map(resourceId -> new RoleMtmResourcePO(roleId, resourceId))
                    .collect(Collectors.toList());
            Db.saveBatch(roleMtmResourcePOList);
        }
    }
}
