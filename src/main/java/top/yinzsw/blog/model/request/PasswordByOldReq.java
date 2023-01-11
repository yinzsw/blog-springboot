package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 更新密码(旧密码)
 *
 * @author yinzsW
 * @since 22/12/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新密码(旧密码)")
public class PasswordByOldReq {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    @Schema(title = "旧密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 64, message = "新密码不能少于{min}位")
    @Schema(title = "新密码")
    private String newPassword;
}
