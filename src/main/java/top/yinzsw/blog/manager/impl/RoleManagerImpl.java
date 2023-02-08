package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.maps.DataMapBuilder;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.model.dto.RoleMapsDTO;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.util.MapQueryUtils;

import java.util.Collections;
import java.util.List;

/**
 * 角色通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/25
 */
@Service
@RequiredArgsConstructor
public class RoleManagerImpl extends ServiceImpl<RoleMapper, RolePO> implements RoleManager {
    private final DataMapBuilder dataMapBuilder;

    @Override
    public List<Long> getEnabledRoleNamesByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        return MapQueryUtils.create(RolePO::getId, roleIds)
                .queryWrapper(q -> q.eq(RolePO::getIsDisabled, false))
                .getValues(RolePO::getId);
    }

    @Override
    public RoleMapsDTO getRoleMapsDTO(List<Long> roleIds) {
        return dataMapBuilder.builder(new RoleMapsDTO())
                .addMap(RoleMapsDTO::setResourceIdsMap, () -> MapQueryUtils.create(RoleMtmResourcePO::getRoleId, roleIds)
                        .getGroupValueMap(RoleMtmResourcePO::getResourceId))
                .addMap(RoleMapsDTO::setMenuIdsMap, () -> MapQueryUtils.create(RoleMtmMenuPO::getRoleId, roleIds)
                        .getGroupValueMap(RoleMtmMenuPO::getMenuId))
                .build();
    }
}
