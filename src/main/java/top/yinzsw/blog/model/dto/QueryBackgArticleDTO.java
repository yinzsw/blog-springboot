package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import java.util.List;

/**
 * 查询后台文章传输模型
 *
 * @author yinzsW
 * @since 23/02/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryBackgArticleDTO {

    /**
     * 文章id
     */
    private List<Long> articleIds;

    /**
     * 搜索关键字
     */
    private String title;

    /**
     * 文章分类id
     */
    private Long categoryId;

    /**
     * 文章状态
     */
    private ArticleStatusEnum articleStatus;

    /**
     * 文章类型
     */
    private ArticleTypeEnum articleType;
}
