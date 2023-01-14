package top.yinzsw.blog.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页模型
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class PageReq {

    /**
     * 页码
     */
    @Min(value = 1, message = "页码不能小于 {value}")
    @Schema(defaultValue = "1")
    @Parameter(description = "页码")
    private Long page;

    /**
     * 条数
     */
    @Min(value = 1, message = "条数不能少于 {min}")
    @Max(value = 30, message = "条数不能多于 {max}")
    @Schema(defaultValue = "10")
    @Parameter(description = "条数")
    private Long size;

    public <T> Page<T> getPager() {
        return new Page<>((page - 1) * size, size);
    }

    public static boolean instanceOf(Object o) {
        return o instanceof PageReq;
    }

    public static PageReq valueOf(Object o) {
        if (instanceOf(o)) {
            return (PageReq) o;
        }
        throw new ClassCastException(String.format("%s<class> 转换 PageReq<class>失败", o.getClass().getName()));
    }
}
