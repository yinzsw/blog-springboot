package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 留言内容
 *
 * @author yinzsW
 * @since 23/01/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "留言")
public class MessageContentReq {

    /**
     * 留言内容
     */
    @NotBlank(message = "留言内容不能为空")
    @Length(max = 256, message = "留言内容字符长度超出最大长度: {max}")
    @Schema(title = "留言内容")
    private String content;
}
