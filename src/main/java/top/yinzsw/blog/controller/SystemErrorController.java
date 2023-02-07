package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.model.vo.ResponseVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 系统异常控制器 部分代码参考自{@link BasicErrorController}
 *
 * @author yinzsW
 * @since 23/02/01
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
@RequiredArgsConstructor
public class SystemErrorController implements ErrorController {
    private final ErrorAttributes errorAttributes;

    @Operation(hidden = true)
    @RequestMapping
    public ResponseVO<?> error(HttpServletRequest request) throws Throwable {
        //抛出异常信息交由全局异常处理器处理
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Throwable throwable = errorAttributes.getError(servletWebRequest);
        if (Objects.nonNull(throwable)) {
            throw throwable;
        }

        //无异常抛出直接返回响应信息
        String uri = (String) servletWebRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI, RequestAttributes.SCOPE_REQUEST);
        Integer statusCode = (Integer) servletWebRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE, RequestAttributes.SCOPE_REQUEST);
        HttpStatus status = Objects.isNull(statusCode) ? HttpStatus.FORBIDDEN : HttpStatus.valueOf(statusCode);

        String msg = String.format("%s %s [%s]", request.getMethod(), uri, status);
        return ResponseVO.fail(ResponseCodeEnum.INVALID_REQUEST, msg);
    }
}
