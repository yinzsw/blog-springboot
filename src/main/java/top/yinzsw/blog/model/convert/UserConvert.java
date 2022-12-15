package top.yinzsw.blog.model.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.dto.UserDetailsDTO;
import top.yinzsw.blog.model.dto.UserInfoDTO;

/**
 * 用户数据模型转换器
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserConvert {
    
    @Mapping(target = ".", source = "userPO")
    UserInfoDTO toUserInfoDTO(UserDetailsDTO userDetailsDTO);
}
