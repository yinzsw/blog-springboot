package top.yinzsw.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.yinzsw.blog.model.po.ResourcePO;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Describe your code
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

    @Test
    void saveOrUpdateTest() {
        var mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        var handlerMethods = mapping.getHandlerMethods();
        var resourcePOS = handlerMethods.entrySet().stream()
                .filter(entry -> entry.getValue().getBeanType().getName().startsWith("top.yinzsw.blog.controller"))
                .map(entry -> {
                    String apiUrl = Objects.requireNonNull(entry.getKey().getPatternsCondition()).getPatterns().toArray(new String[0])[0];
                    String apiRequestName = entry.getKey().getMethodsCondition().getMethods().toArray(new RequestMethod[0])[0].name();
                    String apiName = entry.getValue().getMethod().getName();

                    ResourcePO resourcePO = new ResourcePO();
                    resourcePO.setUrl(apiUrl);
                    resourcePO.setRequestMethod(apiRequestName);
                    resourcePO.setResourceName(apiName);
                    return resourcePO;
                }).collect(Collectors.toList());

        resourcePOS.stream().map(Object::toString).forEach(log::info);

        resourceService.remove(null);
        resourceService.saveOrUpdateBatch(resourcePOS);
    }
}
