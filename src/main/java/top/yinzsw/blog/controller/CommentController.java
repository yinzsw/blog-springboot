package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageCommentVO;
import top.yinzsw.blog.service.CommentService;

import javax.validation.Valid;

/**
 * 评论模块
 *
 * @author yinzsW
 * @since 23/02/09
 */
@Tag(name = "评论模块")
@Validated
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "获取评论列表(摘要)")
    @GetMapping
    public PageCommentVO pageTopicComments(@Valid PageReq pageReq,
                                           @Valid CommentQueryReq commentQueryReq) {
        return commentService.pageTopicComments(pageReq, commentQueryReq);
    }
}
