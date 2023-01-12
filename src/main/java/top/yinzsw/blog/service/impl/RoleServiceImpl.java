package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.RoleMtmMenuManager;
import top.yinzsw.blog.manager.RoleMtmResourceManager;
import top.yinzsw.blog.manager.UserMtmRoleManager;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.model.converter.RoleConverter;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.model.vo.UserRoleVO;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.util.CommonUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:47:44
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePO> implements RoleService {
    private final UserMtmRoleManager userMtmRoleManager;
    private final RoleMtmResourceManager roleMtmResourceManager;
    private final RoleMtmMenuManager roleMtmMenuManager;
    private final RoleConverter roleConverter;

    @Cacheable(key = "'uid '+#userId")
    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        List<Long> roleIds = userMtmRoleManager.listRoleIdsByUserId(userId);
        return getRoleNamesByIds(roleIds);
    }

    @Cacheable(key = "'rid '+#resourceId")
    @Override
    public List<String> getRoleNamesByResourceId(Long resourceId) {
        List<Long> roleIds = roleMtmResourceManager.listRoleIdsByResourceId(resourceId);
        return getRoleNamesByIds(roleIds);
    }

    @Cacheable(key = "'list'")
    @Override
    public List<UserRoleVO> listUserRoles() {
        List<RolePO> rolePOList = lambdaQuery().select(RolePO::getId, RolePO::getRoleLabel).list();
        return roleConverter.toUserRoleVO(rolePOList);
    }

    @Cacheable(key = "'page '+#args")
    @Override
    public PageVO<RoleVO> pageListRoles(PageReq pageReq, String keywords) {
        // 根据关键词查找角色列表
        boolean alpha = CommonUtils.isAlpha(keywords);
        Page<RolePO> rolePOPage = lambdaQuery()
                .select(RolePO::getId, RolePO::getRoleName,
                        RolePO::getRoleLabel, RolePO::getIsDisabled,
                        RolePO::getCreateTime)
                .likeRight(alpha ? RolePO::getRoleLabel : RolePO::getRoleName, keywords)
                .page(new Page<>(pageReq.getOffset(), pageReq.getSize()));

        // 根据角色id获取菜单id列表和资源id列表
        List<Long> roleIds = rolePOPage.getRecords().stream().map(RolePO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            return new PageVO<>(List.of(), rolePOPage.getTotal());
        }

        List<RoleVO> roleVOList = roleMtmMenuManager.asyncGetMappingByRoleIds(roleIds)
                .thenCombine(roleMtmResourceManager.asyncGetMappingByRoleIds(roleIds), (menuMapping, resourceMapping) ->
                        roleConverter.toRoleVO(rolePOPage.getRecords(), menuMapping, resourceMapping))
                .join();

        return new PageVO<>(roleVOList, rolePOPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveOrUpdateRole(RoleReq roleReq) {
        // 更新角色信息
        RolePO rolePO = RolePO.builder()
                .id(roleReq.getRoleId())
                .roleName(roleReq.getRoleName())
                .roleLabel(roleReq.getRoleLabel())
                .build();
        try {
            saveOrUpdate(rolePO);
        } catch (DuplicateKeyException e) {
            throw new BizException(String.format("角色名 %s/%s 已经存在", roleReq.getRoleName(), roleReq.getRoleLabel()));
        }

        // 更新 角色<->资源, 角色<->菜单 映射关系
        roleMtmResourceManager.updateRoleResources(rolePO.getId(), roleReq.getResourceIdList());
        roleMtmMenuManager.updateRoleMenus(rolePO.getId(), roleReq.getMenuIdList());
        return true;
    }

    @Override
    public Boolean deleteRoles(List<Long> roleIdList) {
        // 判断角色下是否有用户
        Long userCount = userMtmRoleManager.countUserByRoleId(roleIdList);
        if (userCount > 0) {
            throw new BizException(String.format("角色id下共存在%d位用户, 不能删除!", userCount));
        }

        return lambdaUpdate().in(RolePO::getId, roleIdList).remove();
    }

    private List<String> getRoleNamesByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<RolePO> rolePOList = lambdaQuery()
                .select(RolePO::getRoleLabel)
                .eq(RolePO::getIsDisabled, false)
                .in(RolePO::getId, roleIds)
                .list();
        return rolePOList.stream().map(RolePO::getRoleLabel).collect(Collectors.toList());
    }
}




