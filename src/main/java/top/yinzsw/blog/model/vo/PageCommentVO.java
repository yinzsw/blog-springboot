package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 评论摘要信息
 *
 * @author yinzsW
 * @since 23/02/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论摘要信息")
public class PageCommentVO {

    /**
     * 用户信息
     */
    @Schema(title = "用户信息")
    private Map<Long, UserVO> users;

    /**
     * 评论列表
     */
    @Schema(title = "评论列表")
    private List<MainCommentDetailVO> mainComments;

    /**
     * 一级评论评论总数
     */
    @Schema(title = "一级评论评论总数")
    private Long mainCommentCount;

    /**
     * 评论总数
     */
    @Schema(title = "评论总数")
    private Long totalCommentCount;
}
