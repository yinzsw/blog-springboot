package top.yinzsw.blog.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.service.RoleService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 接口拦截器
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Component
@RequiredArgsConstructor
public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
    private final RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        long resourceId = JwtManager.getCurrentResourceId();
        if (resourceId == 0L) {
            return null;
        }

        List<Long> roleIds = roleService.getRoleIdsByResourceId(resourceId);
        return Optional.ofNullable(roleIds)
                .map(ids -> ids.isEmpty() ? List.of(0L) : ids)
                .map(ids -> ids.stream().map(Objects::toString).toArray(String[]::new))
                .map(SecurityConfig::createList)
                .orElse(null);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
