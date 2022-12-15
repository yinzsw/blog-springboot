package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户与角色映射表(多对多)
 *
 * @TableName user_mtm_role
 */
@TableName(value = "user_mtm_role")
@Data
public class UserMtmRolePO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}