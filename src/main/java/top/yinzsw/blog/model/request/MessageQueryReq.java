package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 留言查询模型
 *
 * @author yinzsW
 * @since 23/01/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class MessageQueryReq {

    /**
     * 用户id
     */
    @Parameter(description = "用户id")
    private Long userId;

    /**
     * 是否审核
     */
    @Parameter(description = "是否审核通过")
    private Boolean isReview;
}
