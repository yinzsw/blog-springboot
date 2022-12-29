package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.mapper.RoleMtmResourceMapper;
import top.yinzsw.blog.mapper.UserMtmRoleMapper;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.UserMtmRolePO;
import top.yinzsw.blog.service.RoleService;

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
    private final UserMtmRoleMapper userMtmRoleMapper;
    private final RoleMtmResourceMapper roleMtmResourceMapper;


    @Cacheable(key = "'uid '+#userId")
    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        var roleIds = userMtmRoleMapper
                .selectObjs(new LambdaQueryWrapper<UserMtmRolePO>()
                        .select(UserMtmRolePO::getRoleId)
                        .eq(UserMtmRolePO::getUserId, userId));

        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        return listObjs(new LambdaQueryWrapper<RolePO>()
                .select(RolePO::getRoleLabel)
                .eq(RolePO::getIsDisabled, false)
                .in(RolePO::getId, roleIds), Object::toString);
    }

    @Cacheable(key = "'rid '+#resourceId")
    @Override
    public List<String> getRoleNamesByResourceId(Long resourceId) {
        var roleIds = roleMtmResourceMapper
                .selectObjs(new LambdaQueryWrapper<RoleMtmResourcePO>()
                        .select(RoleMtmResourcePO::getRoleId)
                        .eq(RoleMtmResourcePO::getResourceId, resourceId));

        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        return listObjs(new LambdaQueryWrapper<RolePO>()
                .select(RolePO::getRoleLabel)
                .eq(RolePO::getIsDisabled, false)
                .in(RolePO::getId, roleIds), Object::toString);
    }
}




