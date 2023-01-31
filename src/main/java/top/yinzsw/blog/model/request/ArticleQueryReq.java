package top.yinzsw.blog.model.request;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

/**
 * 文章查询模型
 *
 * @author yinzsW
 * @since 23/01/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class ArticleQueryReq {

    /**
     * 搜索关键字
     */
    @Parameter(description = "搜索关键字(标题)")
    private String title;

    /**
     * 文章分类id
     */
    @Parameter(description = "文章分类id")
    private Long categoryId;

    /**
     * 标签id
     */
    @Parameter(description = "标签id")
    private Long tagId;

    /**
     * 文章状态
     */
    @Parameter(description = "文章状态")
    private ArticleStatusEnum articleStatus;

    /**
     * 文章类型
     */
    @Parameter(description = "文章类型")
    private ArticleTypeEnum articleType;
}
