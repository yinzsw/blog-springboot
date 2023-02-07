package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录历史
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "history")
public class HistoryPO implements Serializable {
    /**
     * 登录历史id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * ip地址源
     */
    private String ipSource;

    /**
     * 用户代理字符串
     */
    private String userAgent;

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

    private static final long serialVersionUID = 1L;
}