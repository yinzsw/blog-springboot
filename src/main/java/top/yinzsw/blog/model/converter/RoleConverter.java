package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.dto.RoleMapsDTO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.model.vo.UserRoleVO;

import java.util.List;

/**
 * 角色数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleConverter {

    List<UserRoleVO> toUserRoleVO(List<RolePO> rolePOList);

    List<RoleVO> toRoleVO(List<RolePO> rolePOList, @Context RoleMapsDTO roleMapsDTO);

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator(RolePO origin,
                                 @Context RoleMapsDTO roleMapsDTO,
                                 @TargetType Class<T> targetType) {
        Long roleId = origin.getId();
        List<Long> menuIds = roleMapsDTO.getMenuIdsMap().get(roleId);
        List<Long> resourceIds = roleMapsDTO.getResourceIdsMap().get(roleId);

        if (targetType.isAssignableFrom(RoleVO.class)) {
            return (T) new RoleVO().setMenuIdList(menuIds).setResourceIdList(resourceIds);
        }

        throw new UnsupportedOperationException();
    }
}

