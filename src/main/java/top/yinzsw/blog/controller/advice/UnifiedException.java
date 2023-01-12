package top.yinzsw.blog.controller.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.model.vo.ResponseVO;

import javax.servlet.ServletException;
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
    private @Value("${spring.servlet.multipart.max-file-size}") String maxUploadFileSize;

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseVO<?> systemExceptionHandler(Exception e) {
        log.error("ExceptionHandler", e);
        return ResponseVO.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 可能是HttpMessage转换Java实体失败造成
     *
     * @param e Http消息不可读异常
     * @return 异常信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseVO<?> notValidParamsExceptionHandler(HttpMessageNotReadableException e) {
        if (Objects.isNull(e.getCause()) || Objects.isNull(e.getCause().getMessage())) {
            return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
        }

        String detailMessageForEnum = getDetailMessageForEnum(e.getCause().getMessage());
        return StringUtils.hasText(detailMessageForEnum) ?
                ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, detailMessageForEnum) :
                ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
    }

    /**
     * Bean Validation 异常
     *
     * @param bindingResult 绑定结果模型
     * @return 异常信息
     */
    @ExceptionHandler(BindException.class)
    public ResponseVO<?> notValidParamsExceptionHandler(BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors()) {
            return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
        }

        FieldError fieldError = bindingResult.getFieldErrors().get(0);
        String message = fieldError.getDefaultMessage();
        if (Objects.isNull(message)) {
            return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
        }

        if (fieldError.isBindingFailure()) {
            message = String.format("字段:'%s' 不期待的预期值:'%s'(可能是类型不匹配)", fieldError.getField(), fieldError.getRejectedValue());
        }
        return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, message);
    }

    /**
     * Bean Validation 异常
     *
     * @param e 违反约束异常
     * @return 异常信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseVO<?> notValidParamsExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());

        return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, message);
    }

    /**
     * 请求缺少必要参数
     *
     * @param e 缺少Servlet请求部分异常
     * @return 异常信息
     */
    @ExceptionHandler({
            MissingServletRequestPartException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class
    })
    public ResponseVO<?> missingParamsExceptionHandler(ServletException e) {
        return ResponseVO.fail(ResponseCodeEnum.MISSING_REQUIRED_PARAMS, String.format("请求参数 '%s' 是必须的", getMissParams(e)));
    }

    /**
     * 用户认证异常
     *
     * @param e 错误凭据异常
     * @return 登录失败信息
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseVO<?> authExceptionHandler(AuthenticationException e) {
        return ResponseVO.fail(ResponseCodeEnum.LOGIN_ERROR, e.getMessage());
    }

    /**
     * 文件上传大小异常
     *
     * @param e 文件上传大小异常
     * @return 上传失败信息
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseVO<?> uploadExceptionHandler(MaxUploadSizeExceededException e) {
        return ResponseVO.fail(ResponseCodeEnum.FILE_UPLOAD_ERROR, String.format("上传文件大小不能超过%s", maxUploadFileSize));
    }

    /**
     * 处理服务异常
     *
     * @param e 业务异常
     * @return 异常信息
     */
    @ExceptionHandler(BizException.class)
    public ResponseVO<?> bizExceptionHandler(BizException e) {
        return ResponseVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseVO<?> daoExceptionHandler(DataAccessException e) {
        return ResponseVO.fail(ResponseCodeEnum.DAO_ERROR);
    }

    /**
     * 获取当枚举类型参数接受失败的详细异常信息
     *
     * @param message 原始信息
     * @return 异常信息
     */
    private static String getDetailMessageForEnum(String message) {
        Matcher matcher = Pattern.compile("from String \"(?<errorValue>.*?)\".*?Enum class: (?<rightValues>\\[.*?]).*?\\[\"(?<field>.*?)\"]\\)", Pattern.DOTALL).matcher(message);
        if (!matcher.find()) {
            return null;
        }

        String errorValue = matcher.group("errorValue");
        String rightValues = matcher.group("rightValues");
        String field = matcher.group("field");

        return String.format(">'%s':'%s'<, 期待值: %s", field, errorValue, rightValues);
    }

    /**
     * 获取缺失参数信息
     *
     * @param e 源异常
     * @return 参数信息
     */
    private static String getMissParams(ServletException e) {
        if (e instanceof MissingServletRequestPartException) {
            var partException = (MissingServletRequestPartException) e;
            return partException.getRequestPartName();
        }

        if (e instanceof MissingServletRequestParameterException) {
            var parameterException = (MissingServletRequestParameterException) e;
            return String.format("%s[%s]", parameterException.getParameterName(), parameterException.getParameterType());
        }

        if (e instanceof MissingPathVariableException) {
            var pathVariableException = (MissingPathVariableException) e;
            return String.format("%s[path]", pathVariableException.getVariableName());
        }
        return "?";
    }
}
