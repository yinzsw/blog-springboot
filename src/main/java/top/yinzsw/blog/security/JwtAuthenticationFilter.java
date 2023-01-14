package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yinzsw.blog.core.context.HttpContext;
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
import java.util.List;
import java.util.Objects;

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
    private static final List<String> IGNORE_URIS = List.of("/druid/**", "/swagger-ui/**", "/v3/api-docs/**");
    private final ResourceService resourceService;
    private final JwtManager jwtManager;
    private final HttpContext httpContext;
    private final AntPathMatcher antPathMatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //验证资源是否需要进行Token校验
        String uri = request.getRequestURI();
        boolean isIgnoreUri = !StringUtils.hasText(uri) || IGNORE_URIS.stream().anyMatch(ignoreUri -> antPathMatcher.match(ignoreUri, uri));
        if (isIgnoreUri) {
            filterChain.doFilter(request, response);
            return;
        }

        String method = request.getMethod();
        ResourcePO resourcePO = resourceService.getResourceByUriAndMethod(uri, method);
        boolean isAnonymousResource = Objects.isNull(resourcePO) || resourcePO.getIsAnonymous();
        if (isAnonymousResource) {
            filterChain.doFilter(request, response);
            return;
        }

        //jwt token校验
        String token = request.getHeader(X_TOKEN);
        TokenTypeEnum tokenTypeEnum = REFRESH_URI.equalsIgnoreCase(uri) ? TokenTypeEnum.REFRESH : TokenTypeEnum.ACCESS;
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
        var authenticationToken = new UsernamePasswordAuthenticationToken(claimsDTO.setRid(resourcePO.getId()), null, claimsDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
