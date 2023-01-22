package top.yinzsw.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.constant.MQConst;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.JwtManager;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.dto.ContextDTO;
import top.yinzsw.blog.model.dto.EmailCodeDTO;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;
import top.yinzsw.blog.security.UserDetailsDTO;
import top.yinzsw.blog.service.AuthService;

import java.util.List;
import java.util.UUID;

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
    private final RabbitTemplate rabbitTemplate;


    @Override
    public UserInfoVO login(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authenticate.getPrincipal();

        //创建JWT和用户信息模型
        Long userId = userDetailsDTO.getId();
        List<String> roles = userDetailsDTO.getRoleList();
        TokenVO tokenVO = jwtManager.createTokenVO(userId, roles);
        UserInfoVO userInfoVO = userConverter.toUserInfoVO(userDetailsDTO, tokenVO);

        //更新用户登录信息
        userManager.updateUserLoginInfo(userId, userDetailsDTO.getIpAddress(), userDetailsDTO.getLastLoginTime());
        return userInfoVO;
    }

    @Override
    public TokenVO refreshToken() {
        ContextDTO currentContextDTO = httpContext.getCurrentContextDTO();
        return jwtManager.createTokenVO(currentContextDTO.getUid(), currentContextDTO.getRoles());
    }

    @Override
    public boolean logout() {
        throw new BizException("暂未实现");
    }

    @Override
    public boolean sendEmailCode(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        rabbitTemplate.convertAndSend(MQConst.EMAIL_EXCHANGE, MQConst.EMAIL_CODE_KEY, new EmailCodeDTO(email, code, UserManager.USER_EMAIL_CODE_EXPIRE_TIME));
        userManager.saveEmailVerificationCode(email, code);
        return true;
    }
}
