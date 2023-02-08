package top.yinzsw.blog.core.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPatternParser;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.service.ResourceService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final ResourceService resourceService;
    private final JwtManager jwtManager;
    private final JwtAuthenticationConfig jwtAuthenticationConfig;

    private Long getResourceId(String uri, String method) {
        PathContainer askUriPathContainer = PathContainer.parsePath(uri);
        boolean isIgnoreUri = jwtAuthenticationConfig.getExcludeUris().stream()
                .map(PathPatternParser.defaultInstance::parse)
                .anyMatch(pathPattern -> pathPattern.matches(askUriPathContainer));
        if (isIgnoreUri) {
            return null;
        }

        ResourcePO resourcePO = resourceService.getResourceByUriAndMethod(uri, method);
        if (Objects.isNull(resourcePO) || resourcePO.getIsAnonymous()) {
            return null;
        }

        return resourcePO.getId();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();
        Long resourceId = getResourceId(uri, method);
        if (Objects.isNull(resourceId)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 校验并获取token信息
        boolean isRefreshUri = jwtAuthenticationConfig.isRefreshUri(HttpMethod.valueOf(method), uri);
        JwtContextDTO jwtContextDTO = jwtManager.parseAndGetJwtContext(isRefreshUri);

        // 校验成功处理
        var authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(jwtContextDTO, null, jwtContextDTO.getAuthorities());
        authenticationToken.setDetails(resourceId);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
