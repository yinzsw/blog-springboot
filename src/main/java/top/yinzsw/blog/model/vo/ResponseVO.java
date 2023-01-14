package top.yinzsw.blog.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ResponseCodeEnum;

import static top.yinzsw.blog.enums.ResponseCodeEnum.SUCCESS;

/**
 * 统一响应模型
 *
 * @author yinzsW
 * @since 22/12/15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "统一响应模型")
public class ResponseVO<T> {

    /**
     * 状态码
     */
    @Schema(title = "状态码")
    private Integer code;

    /**
     * 信息
     */
    @Schema(title = "信息")
    private String msg;

    /**
     * 数据
     */
    @Schema(title = "数据")
    private T data;

    public static <T> ResponseVO<T> success() {
        return success(null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return createResponseVO(SUCCESS.getCode(), SUCCESS.getDesc(), data);
    }

    public static <T> ResponseVO<T> fail(ResponseCodeEnum responseCodeEnum) {
        return createResponseVO(responseCodeEnum.getCode(), responseCodeEnum.getDesc(), null);
    }

    public static <T> ResponseVO<T> fail(ResponseCodeEnum responseCodeEnum, String msg) {
        return createResponseVO(responseCodeEnum.getCode(), msg, null);
    }

    public static <T> ResponseVO<T> fail(Integer code, String msg) {
        return createResponseVO(code, msg, null);
    }

    private static <T> ResponseVO<T> createResponseVO(Integer code, String msg, T data) {
        return new ResponseVO<T>().setCode(code).setMsg(msg).setData(data);
    }
}
