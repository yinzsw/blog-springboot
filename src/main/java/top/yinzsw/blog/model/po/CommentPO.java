package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.TopicTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "comment")
public class CommentPO implements Serializable {
    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论用户id
     */
    private Long userId;

    /**
     * 评论主题id
     */
    private Long topicId;

    /**
     * 评论类型 1.文章 2.相册 3.说说
     */
    private TopicTypeEnum topicType;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论点赞量
     */
    private Long likedCount;

    /**
     * 回复的用户id
     */
    private Long replyUserId;

    /**
     * 回复路径枚举
     */
    private String replyCommentIds;

    /**
     * 评论等级
     */
    private Integer level;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 是否审核
     */
    private Boolean isReviewed;

    /**
     * 是否修改过
     */
    private Boolean isModified;

    /**
     * 是否删除  0否 1是
     */
    @TableLogic
    private Boolean isDeleted;

    /**
     * 评论时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}