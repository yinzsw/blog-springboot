package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 标签模型
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "标签模型")
public class TagReq {

    /**
     * 标签id
     */
    @Min(value = 1, message = "不合法的标签id: ${validatedValue}")
    @Schema(title = "标签id")
    private Long id;

    /**
     * 标签名
     */
    @NotBlank(message = "标签名不能为空")
    @Schema(title = "标签名")
    private String tagName;
}
