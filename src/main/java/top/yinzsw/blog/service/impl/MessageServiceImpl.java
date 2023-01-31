package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.WebConfigManager;
import top.yinzsw.blog.mapper.MessageMapper;
import top.yinzsw.blog.model.converter.MessageConverter;
import top.yinzsw.blog.model.po.MessagePO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.po.WebsiteConfigPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.MessageVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.MessageService;
import top.yinzsw.blog.util.MapQueryUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【message(留言表)】的数据库操作Service实现
 * @createDate 2023-01-28 20:21:06
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessagePO> implements MessageService {
    private final WebConfigManager webConfigManager;
    private final MessageConverter messageConverter;

    @Override
    public PageVO<MessageVO> pageMessages(PageReq pageReq) {
        Page<MessagePO> messagePOPage = lambdaQuery()
                .select(MessagePO::getId, MessagePO::getUserId, MessagePO::getMessageContent)
                .eq(MessagePO::getIsReview, true)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(messagePOPage);

        List<MessagePO> messagePOList = messagePOPage.getRecords();
        List<Long> userIds = messagePOList.stream().map(MessagePO::getUserId).distinct().collect(Collectors.toList());
        Map<Long, UserPO> userMap = MapQueryUtils.create(UserPO::getId, userIds).getKeyMap();
        if (userIds.contains(0L)) {
            String userAvatar = webConfigManager.getWebSiteConfig(WebsiteConfigPO::getDefaultUserAvatar);
            UserPO anonymousUser = new UserPO().setNickname("游客").setAvatar(userAvatar);
            userMap.put(0L, anonymousUser);
        }
        List<MessageVO> messageVOList = messageConverter.toMessageVO(messagePOList, userMap);
        return new PageVO<>(messageVOList, messagePOPage.getTotal());
    }
}




