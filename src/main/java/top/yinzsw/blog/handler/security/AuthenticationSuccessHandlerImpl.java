package top.yinzsw.blog.handler.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.model.convert.UserConvert;
import top.yinzsw.blog.model.dto.UserDetailsDTO;
import top.yinzsw.blog.model.dto.UserInfoDTO;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.service.HttpService;
import top.yinzsw.blog.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录成功处理
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Component
@AllArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private final UserConvert userConvert;
    private final UserService userService;
    private final HttpService httpService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authentication.getPrincipal();
        UserInfoDTO userInfoDTO = userConvert.toUserInfoDTO(userDetailsDTO);
        httpService.setResponseBody(ResponseVO.success(userInfoDTO));

        userDetailsDTO.getUserPO().setIpAddress(httpService.getUserIpAddress());
        userService.updateLoginInfo(userDetailsDTO.getUserPO());
    }
}
