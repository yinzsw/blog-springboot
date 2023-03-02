package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;
import top.yinzsw.blog.enums.CommentOrderTypeEnum;
import top.yinzsw.blog.enums.TopicTypeEnum;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 评论查询模型
 *
 * @author yinzsW
 * @since 23/02/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class CommentQueryReq {

    /**
     * 主题id
     */
    @NotNull(message = "主题id不可为空")
    @Min(value = 1, message = "不合法的分类id: ${validatedValue}")
    @Parameter(description = "主题id")
    private Long topicId;

    /**
     * 主题类型
     */
    @Parameter(description = "主题类型")
    private TopicTypeEnum topicType;

    /**
     * 排序方式
     */
    @Parameter(description = "排序方式")
    private CommentOrderTypeEnum orderType;
}
