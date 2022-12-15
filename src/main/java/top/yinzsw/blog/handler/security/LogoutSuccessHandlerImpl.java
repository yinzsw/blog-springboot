package top.yinzsw.blog.handler.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.service.HttpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录成功处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Component
@AllArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private final HttpService httpService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        httpService.setResponseBody(ResponseVO.success());
    }
}
