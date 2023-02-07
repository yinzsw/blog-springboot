package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.MessageManager;
import top.yinzsw.blog.mapper.MessageMapper;
import top.yinzsw.blog.model.dto.QueryBackMessageDTO;
import top.yinzsw.blog.model.po.MessagePO;

import java.util.HashMap;

/**
 * 留言通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/02/05
 */
@Service
public class MessageManagerImpl extends ServiceImpl<MessageMapper, MessagePO> implements MessageManager {
    @Override
    public Page<MessagePO> pageBackgroundMessages(Page<MessagePO> pager, QueryBackMessageDTO queryBackMessageDTO) {
        return lambdaQuery()
                .select(MessagePO::getId, MessagePO::getUserId, MessagePO::getIpAddress, MessagePO::getIpSource,
                        MessagePO::getMessageContent, MessagePO::getIsReviewed, MessagePO::getCreateTime)
                .allEq(new HashMap<>() {{
                    put(MessagePO::getUserId, queryBackMessageDTO.getUserId());
                    put(MessagePO::getIsReviewed, queryBackMessageDTO.getIsReview());
                }}, false)
                .orderByDesc(MessagePO::getId)
                .page(pager);
    }
}
