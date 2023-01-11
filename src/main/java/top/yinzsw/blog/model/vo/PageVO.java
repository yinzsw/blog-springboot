package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据模型
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页数据")
public class PageVO<T> {

    /**
     * 分页列表
     */
    @Schema(title = "记录列表")
    private List<T> records;

    /**
     * 数据总条数
     */
    @Schema(title = "总记录数")
    private Long count;
}
