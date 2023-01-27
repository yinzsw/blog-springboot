package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 分类模型
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "分类模型")
public class CategoryReq {

    /**
     * 分类id
     */
    @Min(value = 1, message = "不合法的分类id: ${validatedValue}")
    @Schema(title = "分类id")
    private Long id;

    /**
     * 分类名
     */
    @NotBlank(message = "分类名不能为空")
    @Length(min = 2, message = "分类名长度不能小于{min}")
    @Schema(title = "分类名")
    private String categoryName;
}
