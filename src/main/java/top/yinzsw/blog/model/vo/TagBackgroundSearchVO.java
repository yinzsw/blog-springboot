package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 后台搜索文章标签
 *
 * @author yinzsW
 * @since 23/01/25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "后台搜索章标签")
public class TagBackgroundSearchVO {

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

    /**
     * 文章量
     */
    @Schema(title = "文章数量")
    private Long articleCount;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
