package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.FriendLinkPO;
import top.yinzsw.blog.model.vo.FriendLinkVO;

import java.util.List;

/**
 * 友链数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendLinkConverter {
    List<FriendLinkVO> toFriendLinkVO(List<FriendLinkPO> friendLinkPOList);
}
