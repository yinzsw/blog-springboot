package top.yinzsw.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import top.yinzsw.blog.manager.RoleMtmResourceManager;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private RoleMtmResourceManager roleMtmResourceManager;

    @Test
    void saveOrUpdateTest() {
        log.info("开始初始化资源...");
        List<ResourcePO> resourcePOS = loadResources();
        log.info("资源初始化完毕!");

        for (ResourcePO resourcePO : resourcePOS) {
            log.info("开始加载资源: [{}] {}", resourcePO.getRequestMethod(), resourcePO.getUri());

            Optional.ofNullable(resourceService.getResourceByUriAndMethod(resourcePO.getUri(), resourcePO.getRequestMethod()))
                    .ifPresentOrElse(resource -> resourceService.lambdaUpdate().eq(ResourcePO::getId, resource.getId()).update(resourcePO),
                            () -> resourceService.save(ConditionSave(resourcePO)));

            roleMtmResourceManager.lambdaQuery()
                    .eq(RoleMtmResourcePO::getResourceId, resourcePO.getId())
                    .eq(RoleMtmResourcePO::getRoleId, 1L)
                    .oneOpt()
                    .ifPresentOrElse(po -> {
                    }, () -> roleMtmResourceManager.save(new RoleMtmResourcePO(1L, resourcePO.getId())));

        }
    }

    @Test
    void clearResources() {
        resourceService.lambdaUpdate().remove();
        roleMtmResourceManager.lambdaUpdate().remove();
    }

    private ResourcePO ConditionSave(ResourcePO resourcePO) {
        if ("/auth/login".equals(resourcePO.getUri())) {
            resourcePO.setIsAnonymous(true);
        }
        return resourcePO;
    }

    /**
     * 加载所有接口
     *
     * @return 接口资源列表
     */
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
