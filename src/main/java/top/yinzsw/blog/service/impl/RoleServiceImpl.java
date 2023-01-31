package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.model.converter.RoleConverter;
import top.yinzsw.blog.model.dto.RoleMapsDTO;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.UserMtmRolePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.RoleDigestVO;
import top.yinzsw.blog.model.vo.RoleSearchVO;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.MapQueryUtils;
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
    private final RoleManager roleManager;
    private final RoleConverter roleConverter;

    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        List<Long> roleIds = MapQueryUtils.create(UserMtmRolePO::getUserId, List.of(userId))
                .getValues(UserMtmRolePO::getRoleId);
        return roleManager.getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<String> getRoleNamesByResourceId(Long resourceId) {
        List<Long> roleIds = MapQueryUtils.create(RoleMtmResourcePO::getResourceId, List.of(resourceId))
                .getValues(RoleMtmResourcePO::getRoleId);
        return roleManager.getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<RoleDigestVO> listDigestRoles() {
        List<RolePO> rolePOList = lambdaQuery().select(RolePO::getId, RolePO::getRoleName).list();
        return roleConverter.toRoleDigestVO(rolePOList);
    }

    @Override
    public PageVO<RoleSearchVO> pageSearchRoles(PageReq pageReq, String keywords) {
        Page<RolePO> rolePOPage = lambdaQuery()
                .select(RolePO::getId, RolePO::getRoleName,
                        RolePO::getRoleLabel, RolePO::getIsDisabled,
                        RolePO::getCreateTime)
                .and(StringUtils.hasText(keywords), q -> q.apply(RolePO.FULL_MATCH, keywords))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(rolePOPage);

        List<RolePO> rolePOList = rolePOPage.getRecords();
        RoleMapsDTO roleMapsDTO = roleManager.getRoleMapsDTO(CommonUtils.toList(rolePOList, RolePO::getId));
        List<RoleSearchVO> roleSearchVOList = roleConverter.toRoleSearchVO(rolePOList, roleMapsDTO);
        return new PageVO<>(roleSearchVOList, rolePOPage.getTotal());
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

        //建立角色与资源和菜单的映射关系
        Long roleId = rolePO.getId();
        Db.lambdaUpdate(RoleMtmMenuPO.class).eq(RoleMtmMenuPO::getRoleId, roleId).remove();
        Db.lambdaUpdate(RoleMtmResourcePO.class).eq(RoleMtmResourcePO::getRoleId, roleId).remove();
        Db.saveBatch(CommonUtils.toList(roleReq.getMenuIds(), menuId -> new RoleMtmMenuPO(roleId, menuId)));
        Db.saveBatch(CommonUtils.toList(roleReq.getResourceIds(), resourceId -> new RoleMtmResourcePO(roleId, resourceId)));
        return true;
    }

    @Override
    public boolean deleteRoles(List<Long> roleIds) {
        // 防止删除角色后, 导致部分用户无法正常使用
        boolean isUsing = Db.lambdaQuery(UserMtmRolePO.class).in(UserMtmRolePO::getRoleId, roleIds).count() > 0L;
        if (isUsing) {
            throw new BizException("该角色下存在用户, 删除失败");
        }
        Db.lambdaUpdate(RoleMtmMenuPO.class).in(RoleMtmMenuPO::getRoleId, roleIds).remove();
        Db.lambdaUpdate(RoleMtmResourcePO.class).in(RoleMtmResourcePO::getRoleId, roleIds).remove();
        return lambdaUpdate().in(RolePO::getId, roleIds).remove();
    }
}




