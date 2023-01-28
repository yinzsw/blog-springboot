package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.mapper.FriendLinkMapper;
import top.yinzsw.blog.model.converter.FriendLinkConverter;
import top.yinzsw.blog.model.po.FriendLinkPO;
import top.yinzsw.blog.model.request.FriendLinkReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.FriendLinkVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.FriendLinkService;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【friend_link(友链表)】的数据库操作Service实现
 * @createDate 2023-01-27 23:08:47
 */
@Service
@RequiredArgsConstructor
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLinkPO> implements FriendLinkService {
    private final FriendLinkConverter friendLinkConverter;

    @Override
    public PageVO<FriendLinkVO> pageSearchFriendLinks(PageReq pageReq, String keywords) {
        Page<FriendLinkPO> friendLinkPOPage = lambdaQuery()
                .select(FriendLinkPO::getId, FriendLinkPO::getLinkName, FriendLinkPO::getLinkAvatar,
                        FriendLinkPO::getLinkAddress, FriendLinkPO::getLinkIntro, FriendLinkPO::getCreateTime)
                .and(StringUtils.hasText(keywords), q -> q.apply(FriendLinkPO.FULL_MATCH, keywords))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(friendLinkPOPage);

        List<FriendLinkVO> friendLinkVOList = friendLinkConverter.toFriendLinkVO(friendLinkPOPage.getRecords());
        return new PageVO<>(friendLinkVOList, friendLinkPOPage.getTotal());
    }

    @Override
    public boolean saveOrUpdateFriendLink(FriendLinkReq friendLinkReq) {
        FriendLinkPO friendLinkPO = friendLinkConverter.toFriendLinkPO(friendLinkReq);
        return saveOrUpdate(friendLinkPO);
    }
}




