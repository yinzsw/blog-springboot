package top.yinzsw.blog.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.util.CommonUtils;

import java.util.Collection;

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
    private static final String[] PADDING_ROLE_NAMES = new String[]{"''"};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        return CommonUtils.getCurrentContextDTO()
                .map(JwtContextDTO::getRid)
                .map(roleService::getRoleNamesByResourceId)
                .map(roleNames -> roleNames.isEmpty() ? PADDING_ROLE_NAMES : roleNames.toArray(String[]::new))
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
