package top.yinzsw.blog.handler.security;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.service.HttpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拒绝用户访问处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Component
@AllArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private HttpService httpService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        httpService.setResponseBody(ResponseVO.fail(ResponseCodeEnum.UNAUTHORIZED));
    }
}
