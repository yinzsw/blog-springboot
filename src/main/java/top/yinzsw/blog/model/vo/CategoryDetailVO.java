package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 文章分类详情模型
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章分类详情模型")
public class CategoryDetailVO {

    /**
     * 分类id
     */
    @Schema(title = "分类id")
    private Long id;

    /**
     * 分类名
     */
    @Schema(title = "分类名")
    private String categoryName;

    /**
     * 该分类下的文章数量
     */
    @Schema(title = "文章数量")
    private Long articleCount;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
