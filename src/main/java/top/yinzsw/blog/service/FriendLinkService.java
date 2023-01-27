package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.FriendLinkPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.FriendLinkVO;
import top.yinzsw.blog.model.vo.PageVO;

/**
 * @author yinzsW
 * @description 针对表【friend_link(友链表)】的数据库操作Service
 * @createDate 2023-01-27 23:08:47
 */
public interface FriendLinkService extends IService<FriendLinkPO> {

    /**
     * 格局关键词获取友链
     *
     * @param pageReq  分页信息
     * @param keywords 关键词
     * @return 友链信息
     */
    PageVO<FriendLinkVO> pageSearchFriendLinks(PageReq pageReq, String keywords);
}
