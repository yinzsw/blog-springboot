package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.RoleMtmResourceManager;
import top.yinzsw.blog.mapper.RoleMtmResourceMapper;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.util.MybatisPlusUtils;

import java.util.Collections;
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
@RequiredArgsConstructor
public class RoleMtmResourceManagerImpl extends ServiceImpl<RoleMtmResourceMapper, RoleMtmResourcePO> implements RoleMtmResourceManager {
    @Override
    public List<Long> listRoleIdsByResourceId(Long resourceId) {
        return MybatisPlusUtils.mappingList(RoleMtmResourcePO::getResourceId, RoleMtmResourcePO::getRoleId, resourceId);
    }

    @Async
    @Override
    public CompletableFuture<Map<Long, List<Long>>> getMappingByRoleId(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return CompletableFuture.completedFuture(Collections.emptyMap());
        }
        Map<Long, List<Long>> mapping = MybatisPlusUtils.mappingGroup(RoleMtmResourcePO::getRoleId, RoleMtmResourcePO::getResourceId, roleIds);
        return CompletableFuture.completedFuture(mapping);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleResources(Long roleId, List<Long> resourceIdList) {
        if (Objects.isNull(roleId) || CollectionUtils.isEmpty(resourceIdList)) {
            return;
        }

        lambdaUpdate().eq(RoleMtmResourcePO::getRoleId, roleId).remove();
        List<RoleMtmResourcePO> roleMtmResourcePOList = resourceIdList.stream()
                .map(resourceId -> new RoleMtmResourcePO(roleId, resourceId))
                .collect(Collectors.toList());

        saveBatch(roleMtmResourcePOList);
    }
}
