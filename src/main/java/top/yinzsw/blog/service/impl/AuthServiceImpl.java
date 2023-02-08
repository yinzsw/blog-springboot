package top.yinzsw.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.security.UserDetailsDTO;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;
import top.yinzsw.blog.service.AuthService;

import java.util.List;

/**
 * 用户认证业务接口实现
 *
 * @author yinzsW
 * @since 22/12/21
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtManager jwtManager;
    private final UserManager userManager;
    private final AuthenticationManager authenticationManager;
    private final HttpContext httpContext;
    private final UserConverter userConverter;

    @Override
    public UserInfoVO login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .unauthenticated(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authenticate.getPrincipal();

        //创建JWT和用户信息模型
        Long userId = userDetailsDTO.getId();
        List<Long> roles = userDetailsDTO.getRoleIds();
        TokenVO tokenVO = jwtManager.createTokenVO(userId, roles);

        String userAgent = httpContext.getUserAgent();
        String ipAddress = httpContext.getUserIpAddress().orElse("");
        userManager.saveUserLoginHistory(userId, userAgent, ipAddress);
        return userConverter.toUserInfoVO(userDetailsDTO, tokenVO);
    }

    @Override
    public TokenVO refreshToken() {
        JwtContextDTO currentJwtContextDTO = JwtManager.getCurrentContextDTO()
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        return jwtManager.createTokenVO(currentJwtContextDTO.getUid(), currentJwtContextDTO.getRoles());
    }

    @Override
    public boolean logout() {
        return jwtManager.blockUserToken();
    }

    @Override
    public boolean sendEmailCode(String email) {
        String code = userManager.sendEmailCode(email);
        userManager.saveEmailVerificationCode(email, code);
        return true;
    }
}
