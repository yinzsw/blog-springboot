package top.yinzsw.blog.manager.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.extension.mybatisplus.service.MappingServiceImpl;
import top.yinzsw.blog.manager.RoleMtmResourceManager;
import top.yinzsw.blog.mapper.RoleMtmResourceMapper;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.util.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 角色,资源关联表 通用处理层实现
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Service
public class RoleMtmResourceManagerImpl extends MappingServiceImpl<RoleMtmResourceMapper, RoleMtmResourcePO> implements RoleMtmResourceManager {
    @Override
    public List<Long> listRoleIdsByResourceId(Long resourceId) {
        List<RoleMtmResourcePO> roleMtmResourcePOList = lambdaQuery()
                .select(RoleMtmResourcePO::getRoleId)
                .eq(RoleMtmResourcePO::getResourceId, resourceId)
                .list();

        return roleMtmResourcePOList.stream().map(RoleMtmResourcePO::getRoleId).collect(Collectors.toList());
    }

    @Async
    @Override
    public CompletableFuture<Map<Long, List<Long>>> asyncGetMappingByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return CompletableFuture.completedFuture(new HashMap<>());
        }

        List<RoleMtmResourcePO> roleMtmResourcePOS = lambdaQuery().in(RoleMtmResourcePO::getRoleId, roleIds).list();
        Map<Long, List<Long>> mapping = CommonUtils.getMapping(roleMtmResourcePOS, RoleMtmResourcePO::getRoleId, RoleMtmResourcePO::getResourceId);
        return CompletableFuture.completedFuture(mapping);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateRoleResources(Long roleId, List<Long> resourceIdList) {
        if (Objects.isNull(roleId) || CollectionUtils.isEmpty(resourceIdList)) {
            return false;
        }

        lambdaUpdate().eq(RoleMtmResourcePO::getRoleId, roleId).remove();
        List<RoleMtmResourcePO> roleMtmResourcePOList = resourceIdList.stream()
                .map(resourceId -> new RoleMtmResourcePO(roleId, resourceId))
                .collect(Collectors.toList());
        return saveBatch(roleMtmResourcePOList);
    }
}
