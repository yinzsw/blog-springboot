package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章归档
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章归档")
public class ArticleArchiveVO {

    /**
     * 文章id
     */
    @Schema(title = "文章id")
    private Integer id;

    /**
     * 文章标题
     */
    @Schema(title = "文章标题")
    private String articleTitle;

    /**
     * 文章发表时间
     */
    @Schema(title = "文章发表时间")
    private LocalDateTime createTime;
}
