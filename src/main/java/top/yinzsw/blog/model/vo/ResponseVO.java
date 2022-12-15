package top.yinzsw.blog.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yinzsw.blog.enums.ResponseCodeEnum;

import static top.yinzsw.blog.enums.ResponseCodeEnum.FAIL;
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
public class ResponseVO<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 数据
     */
    private T data;
    /**
     * 信息
     */
    private String msg;

    public static <T> ResponseVO<T> success() {
        return createResponseVO(SUCCESS, null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return createResponseVO(SUCCESS, data);
    }

    public static <T> ResponseVO<T> fail() {
        return createResponseVO(FAIL, null);
    }

    public static <T> ResponseVO<T> fail(ResponseCodeEnum responseCodeEnum) {
        return createResponseVO(responseCodeEnum, null);
    }

    public static <T> ResponseVO<T> fail(ResponseCodeEnum responseCodeEnum, String detail) {
        String message = String.format("摘要: %s  详情: %s", responseCodeEnum.getDesc(), detail);
        return createResponseVO(responseCodeEnum.getCode(), null, message);
    }

    public static <T> ResponseVO<T> fail(Integer code, String message) {
        return createResponseVO(code, null, message);
    }

    private static <T> ResponseVO<T> createResponseVO(ResponseCodeEnum responseCodeEnum, T data) {
        return createResponseVO(responseCodeEnum.getCode(), data, responseCodeEnum.getDesc());
    }

    private static <T> ResponseVO<T> createResponseVO(Integer code, T data, String message) {
        return new ResponseVO<>(code, data, message);
    }
}
