package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 角色与资源映射表(多对多)
 *
 * @TableName role_mtm_resource
 */
@TableName(value = "role_mtm_resource")
@Data
public class RoleMtmResourcePO implements Serializable {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 资源id
     */
    private Long resourceId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}