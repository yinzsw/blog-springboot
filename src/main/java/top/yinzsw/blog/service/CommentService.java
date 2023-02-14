package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageCommentVO;

/**
 * 评论业务接口
 *
 * @author yinzsW
 * @since 23/02/09
 */

public interface CommentService {

    /**
     * 分页获取评论
     *
     * @param pageReq         分页信息
     * @param commentQueryReq 评论查询模型
     * @return 评论分页模型
     */
    PageCommentVO pageTopicComments(PageReq pageReq, CommentQueryReq commentQueryReq);
}
