package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.exception.BizException;
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

import java.util.Collections;
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
    private final RoleConverter roleConverter;

    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        List<Long> roleIds = roleMapping.listRoleIdsByUserId(userId);
        return getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<String> getRoleNamesByResourceId(Long resourceId) {
        List<Long> roleIds = roleMapping.listRoleIdsByResourceId(resourceId);
        return getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<UserRoleVO> listRoles() {
        List<RolePO> rolePOList = lambdaQuery().select(RolePO::getId, RolePO::getRoleLabel).list();
        return roleConverter.toUserRoleVO(rolePOList);
    }

    @Override
    public PageVO<RoleVO> pageRoles(PageReq pageReq, String keywords) {
        // 根据关键词查找角色列表
        boolean isAlpha = VerifyUtils.getIsAlpha(keywords);
        Page<RolePO> rolePOPage = lambdaQuery()
                .select(RolePO::getId, RolePO::getRoleName,
                        RolePO::getRoleLabel, RolePO::getIsDisabled,
                        RolePO::getCreateTime)
                .likeRight(isAlpha ? RolePO::getRoleLabel : RolePO::getRoleName, keywords)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(rolePOPage);

        // 角色id<->菜单id列表 角色id<->资源id列表
        List<RoleVO> roleVOList = roleMapping.builder(rolePOPage.getRecords())
                .mapMenuIds().mapResourceIds().parallelBuild()
                .mappingList(roleConverter::toRoleVO);

        return new PageVO<>(roleVOList, rolePOPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateRole(RoleReq roleReq) {
        // 更新角色信息
        RolePO rolePO = new RolePO()
                .setId(roleReq.getRoleId())
                .setRoleName(roleReq.getRoleName())
                .setRoleLabel(roleReq.getRoleLabel());
        try {
            saveOrUpdate(rolePO);
        } catch (DuplicateKeyException e) {
            throw new BizException(String.format("角色名 %s/%s 已经存在, 请更换", roleReq.getRoleName(), roleReq.getRoleLabel()));
        }

        // 更新 角色<->资源, 角色<->菜单 映射关系
        if (CollectionUtils.isEmpty(roleReq.getMenuIdList())) {
            roleMapping.deleteMenusMapping(rolePO.getId());
            roleMapping.saveMenus(rolePO.getId(), roleReq.getMenuIdList());
        }
        if (!CollectionUtils.isEmpty(roleReq.getResourceIdList())) {
            roleMapping.deleteResourcesMapping(rolePO.getId());
            roleMapping.saveResources(rolePO.getId(), roleReq.getResourceIdList());
        }
        return true;
    }

    @Override
    public boolean deleteRoles(List<Long> roleIds) {
        // 判断角色下是否有用户
        List<Long> userIds = roleMapping.listUserIds(roleIds);
        if (!CollectionUtils.isEmpty(userIds)) {
            throw new BizException(String.format("角色id下共存在%d位用户, 不能删除!", userIds.size()));
        }

        return lambdaUpdate().in(RolePO::getId, roleIds).remove();
    }

    private List<String> getEnabledRoleNamesByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        return SimpleQuery.list(Wrappers.<RolePO>lambdaQuery()
                .select(RolePO::getRoleLabel)
                .eq(RolePO::getIsDisabled, false)
                .in(RolePO::getId, roleIds), RolePO::getRoleLabel);
    }
}




