package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import top.yinzsw.blog.mapper.RoleMtmResourceMapper;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资源初始化
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Slf4j
@SpringBootTest
public class ResourceServiceTests {
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleMtmResourceMapper roleMtmResourceMapper;
    private static final String LOGIN_URI = "/auth/login";

    @Test
    void saveOrUpdateTest() {
        log.info("开始初始化资源...");
        List<ResourcePO> resourcePOS = loadResources();
        log.info("资源初始化完毕!");

        for (ResourcePO resourcePO : resourcePOS) {
            log.info("开始加载资源: [{}] {}", resourcePO.getRequestMethod(), resourcePO.getUri());
            ResourcePO resource = resourceService.getResourceByUriAndMethod(resourcePO.getUri(), resourcePO.getRequestMethod());
            if (Objects.isNull(resource)) {
                if (LOGIN_URI.equals(resourcePO.getUri())) {
                    resourcePO.setIsAnonymous(true);
                }
                resourceService.save(resourcePO);
            } else {
                resourcePO.setId(resource.getId());
                resourceService.updateById(resourcePO);
            }

            RoleMtmResourcePO roleMtmResourcePO = roleMtmResourceMapper
                    .selectOne(new LambdaQueryWrapper<RoleMtmResourcePO>()
                            .eq(RoleMtmResourcePO::getResourceId, resourcePO.getId())
                            .eq(RoleMtmResourcePO::getRoleId, 1L));

            if (Objects.isNull(roleMtmResourcePO)) {
                RoleMtmResourcePO roleMtmResourcePO1 = new RoleMtmResourcePO();
                roleMtmResourcePO1.setResourceId(resourcePO.getId());
                roleMtmResourcePO1.setRoleId(1L);
                roleMtmResourceMapper.insert(roleMtmResourcePO1);
            }
        }
    }

    @Test
    void clearResources() {
        resourceService.remove(null);
        roleMtmResourceMapper.delete(null);
    }

    private List<ResourcePO> loadResources() {
        var mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        var handlerMethods = mapping.getHandlerMethods();
        return handlerMethods.entrySet().stream()
                .filter(entry -> entry.getValue().getBeanType().getName().startsWith("top.yinzsw.blog.controller"))
                .map(entry -> {
                    String apiUrl = Objects.requireNonNull(entry.getKey().getPathPatternsCondition()).getPatterns().toArray(new PathPattern[0])[0].toString();
                    String apiRequestName = entry.getKey().getMethodsCondition().getMethods().toArray(new RequestMethod[0])[0].name();
                    String apiName = entry.getValue().getMethod().getName();

                    ResourcePO resourcePO = new ResourcePO();
                    resourcePO.setUri(apiUrl);
                    resourcePO.setRequestMethod(apiRequestName);
                    resourcePO.setResourceName(apiName);
                    return resourcePO;
                }).collect(Collectors.toList());
    }
}
