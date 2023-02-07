package top.yinzsw.blog.core.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
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

    private Long verifyAndGetResourceId(String uri, String method) {
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
        Long resourceId = verifyAndGetResourceId(uri, method);
        if (Objects.isNull(resourceId)) {
            filterChain.doFilter(request, response);
            return;
        }


        // 校验并获取token信息
        String token = request.getHeader(jwtAuthenticationConfig.getTokenName());
        boolean isRefreshUri = jwtAuthenticationConfig.getRefreshUri().equals(uri) && RequestMethod.PUT.name().equalsIgnoreCase(method);
        JwtContextDTO jwtContextDTO = jwtManager.parseAndGetJwtContext(token, isRefreshUri);
        jwtContextDTO.setRid(resourceId);

        // 校验成功处理
        var authenticationToken = new UsernamePasswordAuthenticationToken(jwtContextDTO, null, jwtContextDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
