package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章一天的热度信息
 *
 * @author yinzsW
 * @since 23/02/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章一天的热度信息")
public class ArticleDayHotVO {

    /**
     * 点赞量
     */
    @Schema(title = "点赞量")
    private Long likesCount;

    /**
     * 浏览量
     */
    @Schema(title = "浏览量")
    private Long viewsCount;
}
