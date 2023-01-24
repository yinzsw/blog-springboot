package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.manager.mapping.RoleMapping;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.model.converter.RoleConverter;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.model.vo.UserRoleVO;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:47:44
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePO> implements RoleService {
    private final RoleMapping roleMapping;
    private final RoleManager roleManager;
    private final RoleConverter roleConverter;

    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        List<Long> roleIds = roleMapping.listRoleIdsByUserId(userId);
        return roleManager.getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<String> getRoleNamesByResourceId(Long resourceId) {
        List<Long> roleIds = roleMapping.listRoleIdsByResourceId(resourceId);
        return roleManager.getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<UserRoleVO> listRoles() {
        List<RolePO> rolePOList = lambdaQuery().select(RolePO::getId, RolePO::getRoleLabel).list();
        return roleConverter.toUserRoleVO(rolePOList);
    }

    @Override
    public PageVO<RoleVO> pageRoles(PageReq pageReq, String keywords) {
        boolean isAlpha = VerifyUtils.getIsAlpha(keywords);
        Page<RolePO> rolePOPage = lambdaQuery()
                .select(RolePO::getId, RolePO::getRoleName,
                        RolePO::getRoleLabel, RolePO::getIsDisabled,
                        RolePO::getCreateTime)
                .likeRight(isAlpha ? RolePO::getRoleLabel : RolePO::getRoleName, keywords)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(rolePOPage);

        List<RoleVO> roleVOList = roleMapping.builder(rolePOPage.getRecords())
                .mapMenuIds().mapResourceIds().parallelBuild()
                .mappingList(roleConverter::toRoleVO);
        return new PageVO<>(roleVOList, rolePOPage.getTotal());
    }

    @Override
    public boolean updateRoleIsDisabled(Long roleId, Boolean isDisabled) {
        return lambdaUpdate().set(RolePO::getIsDisabled, isDisabled).eq(RolePO::getId, roleId).update();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateRole(RoleReq roleReq) {
        RolePO rolePO = new RolePO()
                .setId(roleReq.getRoleId())
                .setRoleName(roleReq.getRoleName())
                .setRoleLabel(roleReq.getRoleLabel());
        try {
            saveOrUpdate(rolePO);
        } catch (DuplicateKeyException e) {
            throw new BizException(String.format("角色名 %s/%s 已经存在, 请更换", roleReq.getRoleName(), roleReq.getRoleLabel()));
        }

        roleMapping.deleteMenusMapping(rolePO.getId());
        roleMapping.deleteResourcesMapping(rolePO.getId());
        if (!CollectionUtils.isEmpty(roleReq.getMenuIdList())) {
            roleMapping.saveMenus(rolePO.getId(), roleReq.getMenuIdList());
        }
        if (!CollectionUtils.isEmpty(roleReq.getResourceIdList())) {
            roleMapping.saveResources(rolePO.getId(), roleReq.getResourceIdList());
        }
        return true;
    }

    @Override
    public boolean deleteRoles(List<Long> roleIds) {
        // 防止删除角色后, 导致部分用户无法正常使用
        List<Long> userIds = roleMapping.listUserIds(roleIds);
        if (!CollectionUtils.isEmpty(userIds)) {
            throw new BizException(String.format("角色id下共存在%d位用户, 不能删除!", userIds.size()));
        }

        return lambdaUpdate().in(RolePO::getId, roleIds).remove();
    }
}




