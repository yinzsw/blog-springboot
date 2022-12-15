package top.yinzsw.blog.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import top.yinzsw.blog.enums.ResponseCodeEnum;


/**
 * 业务异常
 *
 * @author yinzsW
 * @since 22/12/15
 **/
@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = ResponseCodeEnum.FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(ResponseCodeEnum responseCodeEnum) {
        this.code = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getDesc();
    }
}
