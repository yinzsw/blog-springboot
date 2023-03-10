package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签表
 *
 * @TableName tag
 */
@TableName(value = "tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TagPO implements Serializable {
    /**
     * 标签id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名
     */
    private String tagName;

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
    public static final String FULL_MATCH = "MATCH(tag_name) AGAINST({0} IN BOOLEAN MODE)";

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}