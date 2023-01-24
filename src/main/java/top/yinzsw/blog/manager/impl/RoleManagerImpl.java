package top.yinzsw.blog.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.maps.SimpleMapProvider;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.model.po.RolePO;

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
public class RoleManagerImpl implements RoleManager {
    @Override
    public List<String> getEnabledRoleNamesByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        return new SimpleMapProvider<>(RolePO::getId, roleIds)
                .getValues(RolePO::getRoleLabel, q -> q.eq(RolePO::getIsDisabled, false));
    }
}
