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
 * 点赞表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "liked")
public class LikedPO implements Serializable {

    /**
     * 点赞id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 主题id
     */
    private Long topicId;

    /**
     * 点赞主题 1 文章 2说说 3相册 4评论
     */
    private TopicTypeEnum topicType;

    /**
     * 是否喜欢 0否 1是
     */
    private Boolean isLike;

    /**
     * 是否删除  0否 1是
     */
    @TableLogic
    private Boolean isDeleted;

    /**
     * 创建时间
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