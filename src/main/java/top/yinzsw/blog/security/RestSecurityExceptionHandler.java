package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.util.HttpUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SpringSecurity异常处理器
 *
 * @author yinzsW
 * @since 22/12/24
 */
@Component
@RequiredArgsConstructor
public class RestSecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final HttpUtil httpUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        httpUtil.setResponseBody(ResponseVO.fail(ResponseCodeEnum.UNAUTHENTICATED));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        httpUtil.setResponseBody(ResponseVO.fail(ResponseCodeEnum.UNAUTHORIZED));
    }
}
