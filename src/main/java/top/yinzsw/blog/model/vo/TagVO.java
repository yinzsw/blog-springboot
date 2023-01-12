package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签")
public class TagVO {

    /**
     * 标签id
     */
    @Schema(title = "标签id")
    private Long id;

    /**
     * 标签名
     */
    @Schema(title = "标签名")
    private String tagName;
}
