package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.RoleMtmMenuManager;
import top.yinzsw.blog.mapper.RoleMtmMenuMapper;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.util.CommonUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
            return CompletableFuture.completedFuture(null);
        }

        List<RoleMtmMenuPO> roleMtmMenuPOList = lambdaQuery().in(RoleMtmMenuPO::getRoleId, roleIds).list();
        Map<Long, List<Long>> mapping = CommonUtils.getMapping(roleMtmMenuPOList, RoleMtmMenuPO::getRoleId, RoleMtmMenuPO::getMenuId);
        return CompletableFuture.completedFuture(mapping);
    }
}
