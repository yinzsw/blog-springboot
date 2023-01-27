package top.yinzsw.blog.core.maps.mapping;

import top.yinzsw.blog.core.maps.handler.MapTaskRunnerTemplate;
import top.yinzsw.blog.core.maps.util.MapQueryUtils;
import top.yinzsw.blog.model.dto.RoleMapsDTO;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 角色映射
 *
 * @author yinzsW
 * @since 23/01/22
 */

public final class RoleMapping extends MapTaskRunnerTemplate<RolePO, RoleMapsDTO> {

    public RoleMapping(Executor executor, List<RolePO> rolePOList) {
        super(executor, rolePOList, new RoleMapsDTO());
    }

    private List<Long> getTagIds() {
        return getOriginList().stream().map(RolePO::getId).collect(Collectors.toList());
    }

    public RoleMapping mapMenuIds() {
        addRunnable(() -> getContextDTO().setMenuIdsMap(MapQueryUtils.create(RoleMtmMenuPO::getRoleId, getTagIds())
                .getGroupValueMap(RoleMtmMenuPO::getMenuId)));
        return this;
    }

    public RoleMapping mapResourceIds() {
        addRunnable(() -> {
            Map<Long, List<Long>> resourceIdsMap = MapQueryUtils.create(RoleMtmResourcePO::getRoleId, getTagIds())
                    .getGroupValueMap(RoleMtmResourcePO::getResourceId);
            getContextDTO().setResourceIdsMap(resourceIdsMap);
        });
        return this;
    }
}
