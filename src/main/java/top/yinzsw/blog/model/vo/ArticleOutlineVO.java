package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章信息大纲
 *
 * @author yinzsW
 * @since 23/01/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章信息大纲")
public class ArticleOutlineVO {

    /**
     * 文章id
     */
    @Schema(title = "文章id")
    private Long id;

    /**
     * 文章缩略图
     */
    @Schema(title = "文章缩略图")
    private String articleCover;

    /**
     * 文章标题
     */
    @Schema(title = "文章标题")
    private String articleTitle;
}
