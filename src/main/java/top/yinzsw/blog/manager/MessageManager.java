package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.dto.QueryBackMessageDTO;
import top.yinzsw.blog.model.po.MessagePO;

/**
 * 留言通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/29
 */

public interface MessageManager extends CommonManager<MessagePO> {

    /**
     * 查询后台留言
     *
     * @param pager               分页器
     * @param queryBackMessageDTO 后台留言查询条件
     * @return 后台留言
     */
    Page<MessagePO> pageBackgroundMessages(Page<MessagePO> pager, QueryBackMessageDTO queryBackMessageDTO);
}
