package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.dto.UserLikedDTO;
import top.yinzsw.blog.model.po.UserPO;
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
    UserDetailsDTO toUserDetailDTO(UserPO userPO, List<String> roleList, UserLikedDTO userLikedDTO);

    UserInfoVO toUserInfoVO(UserDetailsDTO userDetailsDTO, TokenVO token);
}
