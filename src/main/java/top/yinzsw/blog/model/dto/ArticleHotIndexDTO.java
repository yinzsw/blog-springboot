package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章热度指数信息
 *
 * @author yinzsW
 * @since 23/01/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ArticleHotIndexDTO {
    /**
     * 点赞量
     */
    private Long likeCount;

    /**
     * 浏览量
     */
    private Long viewsCount;
}
