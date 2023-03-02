package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.model.po.CommentPO;

import java.util.List;

/**
 * 评论分组
 *
 * @author yinzsW
 * @since 23/02/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GroupCommentDTO {
    /**
     * 主评论id
     */
    private Long mainCommentId;

    /**
     * 回复评论数量
     */
    private Long moreCommentCount;

    /**
     * 回复评论列表
     */
    private List<CommentPO> comments;
}
