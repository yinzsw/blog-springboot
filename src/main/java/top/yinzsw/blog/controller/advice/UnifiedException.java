package top.yinzsw.blog.controller.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.model.vo.ResponseVO;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 接口统一异常处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Slf4j
@RestControllerAdvice(basePackages = "top.yinzsw.blog.controller")
public class UnifiedException {
    private static final Pattern ENUM_CONVERT_ERROR_REGEXP = Pattern.compile("from String \"(?<errorValue>.*?)\".*?Enum class: (?<rightValues>\\[.*?]).*?\\[\"(?<field>.*?)\"]\\)", Pattern.DOTALL);

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseVO<?> exceptionHandler(Exception e) {
        log.error("Controller Exception", e);
        return Objects.isNull(e.getMessage()) ? ResponseVO.fail(ResponseCodeEnum.SYSTEM_ERROR) : ResponseVO.fail(ResponseCodeEnum.SYSTEM_ERROR, e.getMessage());
    }

    /**
     * 可能是HttpMessage转换Java实体失败造成
     *
     * @param e Http消息不可读异常
     * @return 异常信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseVO<?> exceptionHandler(HttpMessageNotReadableException e) {
        if (Objects.isNull(e.getCause()) || Objects.isNull(e.getCause().getMessage())) {
            return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
        }

        String message = e.getCause().getMessage();
        Matcher matcher = ENUM_CONVERT_ERROR_REGEXP.matcher(message);
        if (!matcher.find()) {
            return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
        }

        String errorValue = matcher.group("errorValue");
        String rightValues = matcher.group("rightValues");
        String field = matcher.group("field");
        String detail = String.format(">'%s':'%s'<, 期待值: %s", field, errorValue, rightValues);
        return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, detail);
    }

    /**
     * Bean Validation 异常
     *
     * @param e 方法参数无效异常
     * @return 异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVO<?> exceptionHandler(MethodArgumentNotValidException e) {
        var fieldError = e.getBindingResult().getFieldError();
        return Objects.isNull(fieldError) ? ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS) : ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, fieldError.getDefaultMessage());
    }

    /**
     * 用户认证异常
     *
     * @param e 错误凭据异常
     * @return 登录失败信息
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseVO<?> exceptionHandler(BadCredentialsException e) {
        return ResponseVO.fail(ResponseCodeEnum.LOGIN_ERROR, e.getMessage());
    }

    /**
     * 处理服务异常
     *
     * @param e 业务异常
     * @return 异常信息
     */
    @ExceptionHandler(BizException.class)
    public ResponseVO<?> exceptionHandler(BizException e) {
        return ResponseVO.fail(e.getCode(), e.getMessage());
    }
}
