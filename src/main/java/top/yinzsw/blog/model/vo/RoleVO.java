package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户角色模型
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户角色模型")
public class RoleVO {

    /**
     * 角色id
     */
    @Schema(title = "角色id")
    private Long id;

    /**
     * 角色名
     */
    @Schema(title = "角色名(zh)")
    private String roleName;

    /**
     * 角色名(en)
     */
    @Schema(title = "角色名(en)")
    private String roleLabel;
}
