package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 资源表
 *
 * @TableName resource
 */
@TableName(value = "resource")
@Data
public class ResourcePO implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 权限路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 是否匿名访问 0否 1是
     */
    private Boolean isAnonymous;

    /**
     * 创建时间
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