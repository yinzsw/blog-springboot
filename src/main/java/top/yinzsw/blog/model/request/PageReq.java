package top.yinzsw.blog.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页模型
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public class PageReq {

    /**
     * 页码
     */
    @NotNull(message = "页码值不能为空")
    @Min(value = 1, message = "页码不能小于 {value}")
    @Parameter(description = "页码")
    private Long page;

    /**
     * 条数
     */
    @Min(value = 1, message = "条数不能少于 {min}")
    @Max(value = 30, message = "条数不能多于 {max}")
    @Parameter(description = "条数")
    private Long size;

    @JsonIgnore
    public Long getOffset() {
        return (page - 1) * size;
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
