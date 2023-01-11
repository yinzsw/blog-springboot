package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色模型
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户角色模型")
public class UserRoleVO {

    /**
     * 角色id
     */
    @Schema(title = "角色id")
    private Long id;

    /**
     * 角色名
     */
    @Schema(title = "角色名(en)")
    private String roleLabel;
}
