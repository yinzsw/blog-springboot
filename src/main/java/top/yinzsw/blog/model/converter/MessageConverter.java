package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.dto.QueryBackMessageDTO;
import top.yinzsw.blog.model.po.MessagePO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.MessageQueryReq;
import top.yinzsw.blog.model.vo.MessageBackgroundVO;
import top.yinzsw.blog.model.vo.MessageVO;

import java.util.List;
import java.util.Map;

/**
 * 留言数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/28
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageConverter {
    List<MessageVO> toMessageVO(List<MessagePO> messagePOS, @Context Map<Long, UserPO> userMap);

    List<MessageBackgroundVO> toMessageBackgroundVO(List<MessagePO> messagePOList, @Context Map<Long, UserPO> userMap);

    QueryBackMessageDTO toQueryBackMessageDTO(MessageQueryReq messageQueryReq);

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator(MessagePO origin,
                                 @Context Map<Long, UserPO> userMap,
                                 @TargetType Class<T> targetType) {
        Long userId = origin.getUserId();
        UserPO userPO = userMap.get(userId);

        if (targetType.isAssignableFrom(MessageVO.class)) {
            return (T) new MessageVO().setNickname(userPO.getNickname()).setAvatar(userPO.getAvatar());
        }

        if (targetType.isAssignableFrom(MessageBackgroundVO.class)) {
            return (T) new MessageBackgroundVO().setNickname(userPO.getNickname()).setAvatar(userPO.getAvatar());
        }

        throw new UnsupportedOperationException();
    }
}
