package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.util.pattern.PathPatternParser;
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

    @Cacheable(key = "#method+' '+#uri")
    @Override
    public ResourcePO getResourceByUriAndMethod(String uri, String method) {
        //Uri 合法性校验
        PathContainer askUriPathContainer = PathContainer.parsePath(uri);
        if (askUriPathContainer.elements().size() < 2) {
            return null;
        }

        String uriPrefix = askUriPathContainer.subPath(0, 2).value();
        List<ResourcePO> resourcePOList = lambdaQuery()
                .select(ResourcePO::getId, ResourcePO::getUri, ResourcePO::getIsAnonymous)
                .eq(ResourcePO::getRequestMethod, method)
                .likeRight(ResourcePO::getUri, uriPrefix)
                .list();

        return resourcePOList.stream()
                .filter(resourcePO -> PathPatternParser.defaultInstance.parse(resourcePO.getUri()).matches(askUriPathContainer))
                .findFirst()
                .orElse(null);
    }
}




