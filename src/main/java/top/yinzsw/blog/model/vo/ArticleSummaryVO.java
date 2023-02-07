package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 文章摘要信息
 *
 * @author yinzsW
 * @since 23/02/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章摘要信息")
public class ArticleSummaryVO {

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

    /**
     * 发表时间
     */
    @Schema(title = "发表时间")
    private LocalDateTime createTime;
}
