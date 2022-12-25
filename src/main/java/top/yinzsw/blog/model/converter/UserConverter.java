package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.dto.UserInfoDTO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.UserInfoRequest;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.security.UserDetailsDTO;

import java.util.List;

/**
 * 用户数据模型转换器
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserConverter {


    @Mapping(target = ".", source = "userPO")
    @Mapping(target = "roleList", source = "roles")
    @Mapping(target = "talkLikeSet", ignore = true)
    @Mapping(target = "commentLikeSet", ignore = true)
    @Mapping(target = "articleLikeSet", ignore = true)
    UserDetailsDTO toUserDetailDTO(UserPO userPO, List<String> roles);

    UserInfoDTO toUserInfoDTO(UserDetailsDTO userDetailsDTO, TokenVO token);

    @Mapping(target = "id", source = "userId")
    UserPO toUserPO(UserInfoRequest userInfoRequest, Long userId);
}
