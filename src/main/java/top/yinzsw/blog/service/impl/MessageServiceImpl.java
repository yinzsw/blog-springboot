package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.manager.MessageManager;
import top.yinzsw.blog.manager.WebConfigManager;
import top.yinzsw.blog.model.converter.MessageConverter;
import top.yinzsw.blog.model.po.MessagePO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.po.WebsiteConfigPO;
import top.yinzsw.blog.model.request.MessageContentReq;
import top.yinzsw.blog.model.request.MessageQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.MessageBackgroundVO;
import top.yinzsw.blog.model.vo.MessageVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.MessageService;
import top.yinzsw.blog.util.MapQueryUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【message(留言表)】的数据库操作Service实现
 * @createDate 2023-01-28 20:21:06
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageManager messageManager;
    private final WebConfigManager webConfigManager;
    private final MessageConverter messageConverter;
    private final HttpContext httpContext;
    private final IpClient ipClient;

    @Override
    public PageVO<MessageVO> pageMessages(PageReq pageReq) {
        Page<MessagePO> messagePOPage = messageManager.lambdaQuery()
                .select(MessagePO::getId, MessagePO::getUserId, MessagePO::getMessageContent)
                .eq(MessagePO::getIsReviewed, true)
                .page(pageReq.getPager());

        List<MessageVO> messageVOList = messagePO2VOList(messagePOPage, messageConverter::toMessageVO);
        return new PageVO<>(messageVOList, messagePOPage.getTotal());
    }

    @Override
    public PageVO<MessageBackgroundVO> pageBackgroundMessages(PageReq pageReq, MessageQueryReq messageQueryReq) {
        Page<MessagePO> messagePOPage = messageManager.pageBackgroundMessages(pageReq.getPager(), messageConverter.toQueryBackMessageDTO(messageQueryReq));

        List<MessageBackgroundVO> messageBackgroundVOList = messagePO2VOList(messagePOPage, messageConverter::toMessageBackgroundVO);
        return new PageVO<>(messageBackgroundVOList, messagePOPage.getTotal());
    }

    @Override
    public boolean saveMessage(MessageContentReq messageReq) {
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));
        String userIpAddress = httpContext.getUserIpAddress().orElse(null);
        String userIpLocation = ipClient.getIpInfo(userIpAddress).getFirstLocation().orElse(null);
        Boolean isReview = webConfigManager.getWebSiteConfig(WebsiteConfigPO::getEnableAllMessageReview);

        MessagePO messagePO = new MessagePO()
                .setUserId(uid)
                .setMessageContent(messageReq.getContent())
                .setIpAddress(userIpAddress)
                .setIpSource(userIpLocation)
                .setIsReviewed(isReview);
        return messageManager.save(messagePO);
    }

    @Override
    public boolean updateMessagesIsReview(List<Long> messageIds, Boolean isReview) {
        return messageManager.lambdaUpdate().set(MessagePO::getIsReviewed, isReview).in(MessagePO::getId, messageIds).update();
    }

    @Override
    public boolean deleteMessages(List<Long> messageIds) {
        return messageManager.removeByIds(messageIds);
    }

    private <T> List<T> messagePO2VOList(Page<MessagePO> messagePOPage,
                                         BiFunction<List<MessagePO>, Map<Long, UserPO>, List<T>> mapFunction) {
        VerifyUtils.checkIPage(messagePOPage);

        List<MessagePO> messagePOList = messagePOPage.getRecords();
        List<Long> userIds = messagePOList.stream().map(MessagePO::getUserId).distinct().collect(Collectors.toList());
        Map<Long, UserPO> userMap = MapQueryUtils.create(UserPO::getId, userIds).getKeyMap();
        if (userIds.contains(0L)) {
            String userAvatar = webConfigManager.getWebSiteConfig(WebsiteConfigPO::getDefaultUserAvatar);
            UserPO anonymousUser = new UserPO().setNickname("游客").setAvatar(userAvatar);
            userMap.put(0L, anonymousUser);
        }

        return mapFunction.apply(messagePOList, userMap);
    }
}




