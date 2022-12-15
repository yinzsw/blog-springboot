package top.yinzsw.blog.handler.security;

import lombok.AllArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.service.ResourceService;
import top.yinzsw.blog.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口拦截器
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Component
@AllArgsConstructor
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
    private final ResourceService resourceService;
    private final RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        List<ResourcePO> resourcePOS = resourceService.list();

        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<ResourcePO> Resources = resourcePOS.stream()
                .filter(resourcePO -> antPathMatcher.match(resourcePO.getUrl(), uri) && resourcePO.getRequestMethod().equals(method))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(Resources) || Resources.get(0).getIsAnonymous()) {
            return null;
        }

        List<String> roleNames = roleService.getRoleNamesByResourceId(Resources.get(0).getId());
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
