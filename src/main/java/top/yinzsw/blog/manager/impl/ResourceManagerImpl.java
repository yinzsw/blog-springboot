package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import top.yinzsw.blog.manager.ResourceManager;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资源通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceManagerImpl implements ResourceManager {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Transactional(rollbackFor = Exception.class)
    @Async
    @Override
    public void initResources() {
        //移除资源信息
        Db.lambdaUpdate(ResourcePO.class).remove();
        Db.lambdaUpdate(RoleMtmResourcePO.class).remove();

        //加载应用资源列表
        var handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        List<ResourcePO> resourcePOList = handlerMethods.entrySet().stream()
                .filter(entry -> entry.getValue().getBeanType().getName().startsWith("top.yinzsw.blog.controller"))
                .map(entry -> {
                    String apiUrl = Objects.requireNonNull(entry.getKey().getPathPatternsCondition()).getPatterns()
                            .toArray(new PathPattern[0])[0].toString();

                    String apiRequestMethod = entry.getKey().getMethodsCondition()
                            .getMethods().toArray(new RequestMethod[0])[0].name();

                    String apiName = entry.getValue().getMethod().getName();

                    return new ResourcePO().setUri(apiUrl).setRequestMethod(apiRequestMethod).setResourceName(apiName);
                }).collect(Collectors.toList());

        //保存资源信息
        resourcePOList.forEach(resourcePO -> {
            log.info("开始加载资源: [{}] {}", resourcePO.getRequestMethod(), resourcePO.getUri());

            if ("/auth/login".equals(resourcePO.getUri())) {
                resourcePO.setIsAnonymous(true);
            }

            Db.lambdaQuery(ResourcePO.class)
                    .eq(ResourcePO::getUri, resourcePO.getUri())
                    .eq(ResourcePO::getRequestMethod, resourcePO.getRequestMethod())
                    .oneOpt()
                    .ifPresentOrElse(resource -> Db.updateById(resourcePO.setId(resource.getId())), () -> Db.save(resourcePO));

            Db.lambdaQuery(RoleMtmResourcePO.class)
                    .eq(RoleMtmResourcePO::getResourceId, resourcePO.getId())
                    .eq(RoleMtmResourcePO::getRoleId, 1L)
                    .oneOpt()
                    .ifPresentOrElse(po -> new Object(), () -> Db.save(new RoleMtmResourcePO(1L, resourcePO.getId())));
        });
    }
}
