package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.JwtManager;
import top.yinzsw.blog.model.dto.ClaimsDTO;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.service.ResourceService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * jwt 用户认证授权过滤器
 *
 * @author yinzsW
 * @since 22/12/23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String X_TOKEN = "token";
    private static final String REFRESH_URI = "/auth/refresh";
    private final ResourceService resourceService;
    private final JwtManager jwtManager;
    private final HttpContext httpContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //验证资源是否需要进行Token校验
        String uri = request.getRequestURI();
        String method = request.getMethod();
        ResourcePO resourcePO = resourceService.getResourceByUriAndMethod(uri, method);
        boolean isAnonymousResource = Objects.isNull(resourcePO) || resourcePO.getIsAnonymous();
        if (isAnonymousResource) {
            filterChain.doFilter(request, response);
            return;
        }

        //jwt token校验
        String token = request.getHeader(X_TOKEN);
        Optional.ofNullable(token).orElseThrow(() -> new BizException(ResponseCodeEnum.TOKEN_EXPIRED));
        TokenTypeEnum tokenTypeEnum = REFRESH_URI.equalsIgnoreCase(uri) ? TokenTypeEnum.REFRESH : TokenTypeEnum.ACCESS;

        // 校验失败处理
        ClaimsDTO claimsDTO;
        try {
            claimsDTO = jwtManager.parseTokenInfo(token, tokenTypeEnum);
        } catch (BizException e) {
            httpContext.setResponseBody(ResponseVO.fail(e.getCode(), e.getMessage()));
            return;
        } catch (Exception e) {
            log.error("jwtManager#parseTokenInfo 失败, 原因: {}", e.getMessage());
            throw new AuthenticationServiceException("用户认证失败");
        }

        // 校验成功处理
        claimsDTO.setRid(resourcePO.getId());
        UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder().roleList(claimsDTO.getRoles()).build();
        var authenticationToken = new UsernamePasswordAuthenticationToken(claimsDTO, null, userDetailsDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
