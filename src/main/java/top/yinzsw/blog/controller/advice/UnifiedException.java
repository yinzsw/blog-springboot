package top.yinzsw.blog.controller.advice;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.exception.EmptyPageException;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.ResponseVO;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 接口统一异常处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UnifiedException {
    private @Value("${spring.servlet.multipart.max-file-size}") String maxUploadFileSize;
    private final HttpContext httpContext;

    /**
     * 处理系统异常
     *
     * @return 接口异常信息
     */
    @ExceptionHandler(Throwable.class)
    public ResponseVO<?> systemExceptionHandler() {
        httpContext.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseVO.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 请求缺少必要参数
     *
     * @param e 缺少Servlet请求部分异常
     * @return 异常信息
     */
    @ExceptionHandler(ServletException.class)
    public ResponseVO<?> servletExceptionHandler(ServletException e) {
        if (e instanceof MissingRequestValueException || e instanceof MissingServletRequestPartException) {
            httpContext.setStatusCode(HttpStatus.BAD_REQUEST);
            String msg = String.format("请求参数 '%s' 是必须的", getMissParams(e));
            return ResponseVO.fail(ResponseCodeEnum.MISSING_REQUIRED_PARAMS, msg);
        }

        if (e instanceof HttpRequestMethodNotSupportedException) {
            httpContext.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            var requestMethodException = (HttpRequestMethodNotSupportedException) e;
            String msg = String.format("无效的请求方法%s", requestMethodException.getMethod());
            return ResponseVO.fail(ResponseCodeEnum.INVALID_REQUEST, msg);
        }

        httpContext.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseVO.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class, MaxUploadSizeExceededException.class})
    public ResponseVO<?> notValidParamsExceptionHandler(NestedRuntimeException e) {
        httpContext.setStatusCode(HttpStatus.BAD_REQUEST);
        if (e instanceof MaxUploadSizeExceededException) {
            return ResponseVO.fail(ResponseCodeEnum.FILE_UPLOAD_ERROR, String.format("上传文件大小不能超过%s", maxUploadFileSize));
        }

        var name = e instanceof MethodArgumentTypeMismatchException ? ((MethodArgumentTypeMismatchException) e).getName() : null;
        return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, name);
    }

    /**
     * Bean Validation 异常
     *
     * @param bindingResult 绑定结果模型
     * @return 异常信息
     */
    @ExceptionHandler(BindException.class)
    public ResponseVO<?> notValidParamsExceptionHandler(BindingResult bindingResult) {
        httpContext.setStatusCode(HttpStatus.BAD_REQUEST);

        if (!bindingResult.hasFieldErrors()) {
            return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
        }

        FieldError fieldError = bindingResult.getFieldErrors().get(0);
        String message = fieldError.getDefaultMessage();
        if (Objects.isNull(message)) {
            return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS);
        }

        if (fieldError.isBindingFailure()) {
            message = String.format("字段:'%s' 不是期待的预期值:'%s'(可能是类型不匹配)", fieldError.getField(), fieldError.getRejectedValue());
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
        httpContext.setStatusCode(HttpStatus.BAD_REQUEST);

        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        return ResponseVO.fail(ResponseCodeEnum.NOT_VALID_PARAMS, message);
    }

    /**
     * 用户认证异常
     *
     * @param e 身份验证异常
     * @return 认证失败信息
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseVO<?> authExceptionHandler(AuthenticationException e) {
        httpContext.setStatusCode(HttpStatus.UNAUTHORIZED);
        if (e instanceof CredentialsExpiredException) {
            return ResponseVO.fail(ResponseCodeEnum.AUTHENTICATED_EXPIRED, e.getMessage());
        }
        return ResponseVO.fail(ResponseCodeEnum.AUTHENTICATED_FAIL, e.getMessage());
    }

    /**
     * 用户访问异常
     *
     * @param e 拒绝访问异常
     * @return 访问失败信息
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseVO<?> authExceptionHandler(AccessDeniedException e) {
        httpContext.setStatusCode(HttpStatus.FORBIDDEN);
        return ResponseVO.fail(ResponseCodeEnum.FORBIDDEN, e.getMessage());
    }

    /**
     * 处理服务异常
     *
     * @param e 业务异常
     * @return 异常信息
     */
    @ExceptionHandler(BizException.class)
    public ResponseVO<?> bizExceptionHandler(BizException e) {
        httpContext.setStatusCode(HttpStatus.OK);
        return ResponseVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(EmptyPageException.class)
    public ResponseVO<?> emptyPageExceptionHandler(EmptyPageException e) {
        httpContext.setStatusCode(HttpStatus.OK);
        return ResponseVO.success(new PageVO<>(Collections.emptyList(), e.getTotalCount()));
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
