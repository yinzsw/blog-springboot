package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.service.ResourceService;
import top.yinzsw.blog.service.RoleService;

import javax.servlet.http.HttpServletRequest;
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
    private final ResourceService resourceService;
    private final RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 查询当前资源信息
        ResourcePO resourcePO = resourceService.getResourceByUriAndMethod(uri, method);
        if (Objects.isNull(resourcePO) || resourcePO.getIsAnonymous()) {
            return null;
        }

        // 查询可以访问此资源的角色列表
        List<String> roleNames = roleService.getRoleNamesByResourceId(resourcePO.getId());
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
