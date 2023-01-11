package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色与菜单映射表(多对多)
 *
 * @TableName role_mtm_menu
 */
@TableName(value = "role_mtm_menu")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleMtmMenuPO implements Serializable {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private Long menuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}