package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 留言表
 *
 * @TableName message
 */
@TableName(value = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MessagePO implements Serializable {
    /**
     * 留言id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * ip地址源
     */
    private String ipSource;

    /**
     * 是否审核
     */
    private Boolean isReview;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean isDeleted;

    /**
     * 发布时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}