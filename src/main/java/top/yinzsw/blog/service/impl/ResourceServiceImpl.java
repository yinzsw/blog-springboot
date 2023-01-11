package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.mapper.ResourceMapper;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.service.ResourceService;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Service实现
 * @createDate 2022-12-15 15:00:20
 */
@Service
@CacheConfig(cacheNames = "resource")
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourcePO> implements ResourceService {
    private final AntPathMatcher antPathMatcher;

    @Cacheable(key = "#method+' '+#uri")
    @Override
    public ResourcePO getResourceByUriAndMethod(String uri, String method) {
        //Uri 合法性校验
        if (!(StringUtils.hasText(uri) && uri.startsWith("/") && uri.length() > 2)) {
            return null;
        }

        String uriPrefix = "/".concat(uri.split("/")[1]);
        List<ResourcePO> resourcePOList = lambdaQuery()
                .select(ResourcePO::getId, ResourcePO::getUri, ResourcePO::getIsAnonymous)
                .eq(ResourcePO::getRequestMethod, method)
                .likeRight(ResourcePO::getUri, uriPrefix)
                .list();
        return resourcePOList.stream()
                .filter(resourcePO -> antPathMatcher.match(resourcePO.getUri(), uri))
                .findFirst()
                .orElse(null);
    }
}




