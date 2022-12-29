package top.yinzsw.blog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.HttpContext;
import top.yinzsw.blog.manager.JwtManager;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.dto.ClaimsDTO;
import top.yinzsw.blog.model.dto.UserInfoDTO;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.security.UserDetailsDTO;
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
    private final AuthenticationManager authenticationManager;
    private final UserManager userManager;
    private final JwtManager jwtManager;
    private final HttpContext httpContext;
    private final UserConverter userConverter;

    @SneakyThrows
    @Override
    public UserInfoDTO login(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authenticate.getPrincipal();

        //创建JWT和用户信息模型
        Long userId = userDetailsDTO.getId();
        List<String> roles = userDetailsDTO.getRoleList();
        TokenVO tokenVO = jwtManager.createTokenVO(userId, roles);
        UserInfoDTO userInfoDTO = userConverter.toUserInfoDTO(userDetailsDTO, tokenVO);

        //更新用户登录信息
        userManager.asyncUpdateUserLoginInfo(userId, userDetailsDTO.getIpAddress(), userDetailsDTO.getLastLoginTime());
        return userInfoDTO;
    }

    @Override
    public TokenVO refreshToken() {
        ClaimsDTO currentClaimsDTO = httpContext.getCurrentClaimsDTO();
        return jwtManager.createTokenVO(currentClaimsDTO.getUid(), currentClaimsDTO.getRoles());
    }

    @Override
    public Boolean logout() {
        throw new BizException("暂未实现");
    }
}
