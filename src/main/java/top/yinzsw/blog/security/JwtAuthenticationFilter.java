package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.HttpContext;
import top.yinzsw.blog.manager.JwtManager;
import top.yinzsw.blog.model.dto.ClaimsDTO;
import top.yinzsw.blog.model.vo.ResponseVO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt 用户认证授权过滤器
 *
 * @author yinzsW
 * @since 22/12/23
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String X_TOKEN = "token";
    private static final String REFRESH_URI = "/auth/refresh";
    private final JwtManager jwtManager;
    private final HttpContext httpContext;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(X_TOKEN);
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT Token 校验
        String uri = request.getRequestURI();
        TokenTypeEnum tokenTypeEnum = REFRESH_URI.equals(uri) ? TokenTypeEnum.REFRESH : TokenTypeEnum.ACCESS;
        ClaimsDTO claimsDTO;
        try {
            claimsDTO = jwtManager.parseTokenInfo(token, tokenTypeEnum);
        } catch (BizException e) {
            httpContext.setResponseBody(ResponseVO.fail(e.getCode(), e.getMessage()));
            return;
        } catch (Exception e) {
            throw new AuthenticationServiceException("用户认证失败");
        }

        // 校验成功, 设置认证上下文信息
        UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder().roleList(claimsDTO.getRoles()).build();
        var authenticationToken = new UsernamePasswordAuthenticationToken(claimsDTO, null, userDetailsDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
