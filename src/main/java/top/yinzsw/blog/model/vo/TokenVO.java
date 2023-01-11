package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TOKEN
 *
 * @author yinzsW
 * @since 22/12/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "令牌信息")
public class TokenVO {

    @Schema(title = "认证token")
    private String accessToken;

    @Schema(title = "刷新token")
    private String refreshToken;
}
