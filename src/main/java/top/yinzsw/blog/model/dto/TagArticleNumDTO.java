package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 标签文章数量
 *
 * @author yinzsW
 * @since 23/02/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TagArticleNumDTO {

    /**
     * 分类id
     */
    private Long tagId;

    /**
     * 文章数量
     */
    private Long articleCount;
}
