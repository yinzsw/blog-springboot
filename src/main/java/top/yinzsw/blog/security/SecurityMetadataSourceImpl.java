package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.model.dto.ContextDTO;
import top.yinzsw.blog.service.RoleService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(principal) || !(principal instanceof ContextDTO)) {
            return null;
        }

        // 查询可以访问此资源的角色列表
        Long rid = ((ContextDTO) principal).getRid();
        List<String> roleNames = roleService.getRoleNamesByResourceId(rid);
        return SecurityConfig.createList(roleNames.toArray(new String[0]));
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
