package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询后台留言传输模型
 *
 * @author yinzsW
 * @since 23/02/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryBackMessageDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否审核
     */
    private Boolean isReview;
}
