package top.yinzsw.blog.manager.mapping;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.maps.AbstractMapsBuilder;
import top.yinzsw.blog.core.maps.SimpleMapProvider;
import top.yinzsw.blog.model.dto.RoleMapsDTO;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 角色映射
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Service
@RequiredArgsConstructor
public class RoleMapping {
    private final ThreadPoolTaskExecutor taskExecutor;

    public Builder builder(List<RolePO> rolePOList) {
        return new Builder(rolePOList, new RoleMapsDTO());
    }

    public List<Long> listRoleIdsByUserId(Long userId) {
        return new SimpleMapProvider<>(UserMtmRolePO::getUserId, List.of(userId)).getValues(UserMtmRolePO::getRoleId);
    }

    public List<Long> listRoleIdsByResourceId(Long resourceId) {
        return new SimpleMapProvider<>(RoleMtmResourcePO::getResourceId, List.of(resourceId)).getValues(RoleMtmResourcePO::getRoleId);
    }

    public List<Long> listUserIds(List<Long> roleIds) {
        return new SimpleMapProvider<>(UserMtmRolePO::getRoleId, roleIds).getValues(UserMtmRolePO::getUserId);
    }

    public void saveResources(Long roleId, List<Long> resourceIdList) {
        List<RoleMtmResourcePO> roleMtmResourcePOList = resourceIdList.stream()
                .map(resourceId -> new RoleMtmResourcePO(roleId, resourceId))
                .collect(Collectors.toList());
        Db.saveBatch(roleMtmResourcePOList);
    }

    public void saveMenus(Long roleId, List<Long> menuIdList) {
        if (Objects.isNull(roleId) || CollectionUtils.isEmpty(menuIdList)) {
            return;
        }

        Db.lambdaUpdate(RoleMtmMenuPO.class).eq(RoleMtmMenuPO::getRoleId, roleId).remove();
        List<RoleMtmMenuPO> roleMtmMenuPOList = menuIdList.stream()
                .map(menuId -> new RoleMtmMenuPO(roleId, menuId))
                .collect(Collectors.toList());
        Db.saveBatch(roleMtmMenuPOList);
    }

    public void deleteResourcesMapping(Long roleId) {
        Db.lambdaUpdate(RoleMtmResourcePO.class).eq(RoleMtmResourcePO::getRoleId, roleId).remove();
    }

    public void deleteMenusMapping(Long roleId) {
        Db.lambdaUpdate(RoleMtmMenuPO.class).eq(RoleMtmMenuPO::getRoleId, roleId).remove();
    }

    public class Builder extends AbstractMapsBuilder<RolePO, RoleMapsDTO> {

        protected Builder(List<RolePO> originList, RoleMapsDTO context) {
            super(originList, context);
        }

        @Override
        protected Executor getExecutor() {
            return taskExecutor;
        }

        public Builder mapMenuIds() {
            addRunnable(() -> {
                List<Long> roleIdList = getIds(RolePO::getId);
                context.setMenuIdsMap(new SimpleMapProvider<>(RoleMtmMenuPO::getRoleId, roleIdList)
                        .getGroupValueMap(RoleMtmMenuPO::getMenuId));
            });
            return this;
        }

        public Builder mapResourceIds() {
            addRunnable(() -> {
                List<Long> roleIdList = getIds(RolePO::getId);
                context.setResourceIdsMap(new SimpleMapProvider<>(RoleMtmResourcePO::getRoleId, roleIdList)
                        .getGroupValueMap(RoleMtmResourcePO::getResourceId));
            });
            return this;
        }
    }
}
