package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友链表
 *
 * @TableName friend_link
 */
@TableName(value = "friend_link")
@Data
public class FriendLinkPO implements Serializable {
    /**
     * 友链id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * 友链头像
     */
    private String linkAvatar;

    /**
     * 友链地址
     */
    private String linkAddress;

    /**
     * 友链介绍
     */
    private String linkIntro;

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

    /**
     * FULL_TEXT_MATCH_SQL
     */
    @TableField(exist = false)
    public static final String FULL_MATCH = "MATCH(link_name,link_intro) AGAINST({0} IN BOOLEAN MODE)";

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}