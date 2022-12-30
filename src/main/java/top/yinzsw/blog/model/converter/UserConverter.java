package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.UserInfoRequest;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;
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

    UserInfoVO toUserInfoVO(UserDetailsDTO userDetailsDTO, TokenVO token);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "loginType", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "isDisabled", ignore = true)
    @Mapping(target = "ipSource", ignore = true)
    @Mapping(target = "ipAddress", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "id", source = "userId")
    UserPO toUserPO(UserInfoRequest userInfoRequest, Long userId);
}
