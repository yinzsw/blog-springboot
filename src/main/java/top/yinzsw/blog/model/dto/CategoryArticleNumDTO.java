package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 分类文章数量
 *
 * @author yinzsW
 * @since 23/02/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CategoryArticleNumDTO {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 文章数量
     */
    private Long articleCount;
}
