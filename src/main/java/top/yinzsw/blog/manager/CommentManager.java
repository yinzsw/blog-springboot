package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.yinzsw.blog.enums.TopicTypeEnum;
import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.CommentPO;
import top.yinzsw.blog.model.request.CommentQueryReq;

/**
 * 评论通用业务处理层
 *
 * @author yinzsW
 * @since 23/02/13
 */

public interface CommentManager extends CommonManager<CommentPO> {
    Page<Long> pageTopicMainCommentIds(Page<CommentPO> pager, CommentQueryReq commentQueryReq);

    long getCommentCount(Long topicId, TopicTypeEnum topicType);
}
