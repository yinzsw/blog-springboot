package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.model.vo.UserRoleVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleConverter {

    List<UserRoleVO> toUserRoleVO(List<RolePO> rolePOList);

    RoleVO toRoleVO(RolePO rolePOList, List<Long> menuIdList, List<Long> resourceIdList);

    default List<RoleVO> toRoleVO(List<RolePO> rolePOList, Map<Long, List<Long>> menuIdMapping, Map<Long, List<Long>> resourceIdMapping) {
        return rolePOList.stream()
                .map(rolePO -> toRoleVO(rolePO, menuIdMapping.get(rolePO.getId()), resourceIdMapping.get(rolePO.getId())))
                .collect(Collectors.toList());
    }
}

