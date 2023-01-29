package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.MessagePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.MessageVO;
import top.yinzsw.blog.model.vo.PageVO;

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
}
