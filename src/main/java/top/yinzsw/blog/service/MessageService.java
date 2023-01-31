package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.MessagePO;
import top.yinzsw.blog.model.request.MessageQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.MessageBackgroundVO;
import top.yinzsw.blog.model.vo.MessageVO;
import top.yinzsw.blog.model.vo.PageVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【message(留言表)】的数据库操作Service
 * @createDate 2023-01-28 20:21:06
 */
public interface MessageService extends IService<MessagePO> {

    /**
     * 查询留言列表
     *
     * @param pageReq 分页信息
     * @return 留言列表
     */
    PageVO<MessageVO> pageMessages(PageReq pageReq);

    /**
     * 查询留言列表
     *
     * @param pageReq         分页信息
     * @param messageQueryReq 留言查询
     * @return 留言列表
     */
    PageVO<MessageBackgroundVO> pageBackgroundMessages(PageReq pageReq, MessageQueryReq messageQueryReq);

    /**
     * 批量审核留言
     *
     * @param messageIds 留言审核列表
     * @param isReview   是否通过
     * @return 是否成功
     */
    boolean updateMessagesIsReview(List<Long> messageIds, Boolean isReview);

    /**
     * 批量删除留言
     *
     * @param messageIds 留言id列表
     * @return 是否成功
     */
    boolean deleteMessages(List<Long> messageIds);
}
