package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 评论
 *
 * @author yinzsW
 * @since 23/02/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论")
public class CommentVO {
    /**
     * 评论id
     */
    @Schema(title = "评论id")
    private Long id;

    /**
     * 用户id
     */
    @Schema(title = "用户id")
    private Long userId;

    /**
     * 回复的用户id
     */
    @Schema(title = "回复的用户id")
    private Long replyUserId;

    /**
     * 评论等级
     */
    @Schema(title = "评论等级")
    private Integer level;

    /**
     * 评论内容
     */
    @Schema(title = "评论内容")
    private String commentContent;

    /**
     * 点赞量
     */
    @Schema(title = "点赞量")
    private Long likedCount;

    /**
     * 是否置顶
     */
    @Schema(title = "是否置顶")
    private Boolean isTop;

    /**
     * 是否修改过
     */
    @Schema(title = "是否修改过")
    private Boolean isModified;

    /**
     * 创建时间
     */
    @Schema(title = "评论id")
    private LocalDateTime createTime;
}
