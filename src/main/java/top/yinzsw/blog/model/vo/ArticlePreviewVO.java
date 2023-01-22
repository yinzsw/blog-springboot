package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章预览
 *
 * @author yinzsW
 * @since 23/01/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章预览")
public class ArticlePreviewVO {

    /**
     * 文章id
     */
    @Schema(title = "文章id")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(title = "文章标题")
    private String articleTitle;

    /**
     * 文章缩略图
     */
    @Schema(title = "文章缩略图")
    private String articleCover;

    /**
     * 发表时间
     */
    @Schema(title = "发表时间")
    private LocalDateTime createTime;

    /**
     * 文章分类id
     */
    @Schema(title = "文章分类id")
    private Long categoryId;

    /**
     * 文章分类名
     */
    @Schema(title = "文章分类名")
    private String categoryName;

    /**
     * 文章标签
     */
    @Schema(title = "文章标签")
    private List<TagVO> tags;
}
