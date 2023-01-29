package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.Map;

/**
 * 分类映射信息传输模型
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CategoryMapsDTO {

    /**
     * 分类id->文章数量
     */
    private Map<Long, Long> articleCountMap = Collections.emptyMap();
}
