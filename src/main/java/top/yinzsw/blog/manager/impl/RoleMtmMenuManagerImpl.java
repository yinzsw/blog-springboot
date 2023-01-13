package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.RoleMtmMenuManager;
import top.yinzsw.blog.mapper.RoleMtmMenuMapper;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.util.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 角色,菜单关联表 通用处理层实现
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Service
public class RoleMtmMenuManagerImpl extends ServiceImpl<RoleMtmMenuMapper, RoleMtmMenuPO> implements RoleMtmMenuManager {
    @Async
    @Override
    public CompletableFuture<Map<Long, List<Long>>> asyncGetMappingByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return CompletableFuture.completedFuture(new HashMap<>());
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<RoleMtmMenuPO> roleMtmMenuPOList = lambdaQuery().in(RoleMtmMenuPO::getRoleId, roleIds).list();
        Map<Long, List<Long>> mapping = CommonUtils.getMapping(roleMtmMenuPOList, RoleMtmMenuPO::getRoleId, RoleMtmMenuPO::getMenuId);
        return CompletableFuture.completedFuture(mapping);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateRoleMenus(Long roleId, List<Long> menuIdList) {
        if (Objects.isNull(roleId) || CollectionUtils.isEmpty(menuIdList)) {
            return false;
        }

        lambdaUpdate().eq(RoleMtmMenuPO::getRoleId, roleId).remove();
        List<RoleMtmMenuPO> roleMtmMenuPOList = menuIdList.stream()
                .map(menuId -> new RoleMtmMenuPO(roleId, menuId))
                .collect(Collectors.toList());
        return saveBatch(roleMtmMenuPOList);
    }
}
