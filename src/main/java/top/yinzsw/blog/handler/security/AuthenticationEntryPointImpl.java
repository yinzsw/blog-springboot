package top.yinzsw.blog.handler.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.service.HttpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户未登录处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Component
@AllArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final HttpService httpService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        httpService.setResponseBody(ResponseVO.fail(ResponseCodeEnum.AUTHENTICATED_ERROR));
    }
}
