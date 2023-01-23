package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户点赞信息
 *
 * @author yinzsW
 * @since 23/01/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserLikedDTO {

    /**
     * 点赞说说集合
     */
    private List<Long> likedTalkSet;

    /**
     * 点赞文章集合
     */
    private List<Long> likedArticleSet;

    /**
     * 点赞评论集合
     */
    private List<Long> likedCommentSet;
}
