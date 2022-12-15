package top.yinzsw.blog.handler.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.service.HttpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录失败处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Component
@AllArgsConstructor
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private final HttpService httpService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        httpService.setResponseBody(ResponseVO.fail(ResponseCodeEnum.LOGIN_ERROR, exception.getMessage()));
    }
}
