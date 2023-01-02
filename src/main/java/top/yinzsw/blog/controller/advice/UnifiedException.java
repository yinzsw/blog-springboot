package top.yinzsw.blog.controller.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.model.vo.ResponseVO;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 接口统一异常处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Slf4j
@RestControllerAdvice
public class UnifiedException {
    private static final Pattern ENUM_CONVERT_ERROR_REGEXP = Pattern.compile("from String \"(?<errorValue>.*?)\".*?Enum class: (?<rightValues>\\[.*?]).*?\\[\"(?<field>.*?)\"]\\)", Pattern.DOTALL);
    private @Value("${spring.servlet.multipart.max-file-size}") String maxUploadFileSize;

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseVO<?> exceptionHandler(Exception e) {
        log.error("Controller Exception", e);
        return Objects.isNull(e.getMessage()) ?
                ResponseVO.fail(ResponseCodeEnum.SYSTEM_ERROR) :
                ResponseVO.fail(ResponseCodeEnum.SYSTEM_ERROR, e.getMessage());
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
     * 请求缺少必要参数
     *
     * @param e 缺少Servlet请求部分异常
     * @return 异常信息
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseVO<?> exceptionHandler(MissingServletRequestPartException e) {
        return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, String.format("请求参数 '%s' 是必须的", e.getRequestPartName()));
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
        return Objects.isNull(fieldError) ?
                ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS) :
                ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, fieldError.getDefaultMessage());
    }

    /**
     * Bean Validation 异常
     *
     * @param e 违反约束异常
     * @return 异常信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseVO<?> exceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, message);
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
     * 文件上传大小异常
     *
     * @param e 文件上传大小异常
     * @return 上传失败信息
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseVO<?> exceptionHandler(MaxUploadSizeExceededException e) {
        return ResponseVO.fail(ResponseCodeEnum.FAIL, String.format("文件大小不能超过%s", maxUploadFileSize));
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
