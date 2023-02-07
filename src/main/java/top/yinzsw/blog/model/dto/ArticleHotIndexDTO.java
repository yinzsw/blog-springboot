package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章热度信息
 *
 * @author yinzsW
 * @since 23/01/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ArticleHotIndexDTO {

    /**
     * 点赞量
     */
    private Long likesCount;

    /**
     * 浏览量
     */
    private Long viewsCount;

}
