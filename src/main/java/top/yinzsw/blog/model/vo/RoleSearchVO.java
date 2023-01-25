package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户角色
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户角色")
public class RoleSearchVO {
    /**
     * 角色id
     */
    @Schema(title = "角色id")
    private Long id;

    /**
     * 角色名(zh)
     */
    @Schema(title = "角色名(zh)")
    private String roleName;

    /**
     * 角色名(en)
     */
    @Schema(title = "角色名(en)")
    private String roleLabel;

    /**
     * 是否禁用
     */
    @Schema(title = "是否禁用")
    private Boolean isDisabled;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 菜单id列表
     */
    @Schema(title = "菜单id列表", nullable = true)
    private List<Long> menuIdList;

    /**
     * 资源id列表
     */
    @Schema(title = "资源id列表", nullable = true)
    private List<Long> resourceIdList;
}
