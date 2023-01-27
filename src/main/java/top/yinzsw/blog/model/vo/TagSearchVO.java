package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 后台文章标签
 *
 * @author yinzsW
 * @since 23/01/25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章标签")
public class TagSearchVO {

    /**
     * 标签id
     */
    private Long id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 文章量
     */
    private Long articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
