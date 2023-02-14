package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedList;

/**
 * 一级评论详情
 *
 * @author yinzsW
 * @since 23/02/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "一级评论详情")
public class MainCommentDetailVO {

    /**
     * 一级评论id
     */
    @Schema(title = "一级评论")
    private CommentVO mainComment;

    /**
     * 评论回复数量
     */
    @Schema(title = "评论回复数量")
    private Long moreCommentCount;

    /**
     * 评论
     */
    @Schema(title = "评论列表")
    private LinkedList<CommentVO> comments;
}
