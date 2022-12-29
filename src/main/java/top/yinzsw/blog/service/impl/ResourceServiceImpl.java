package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.mapper.ResourceMapper;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.service.ResourceService;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Service实现
 * @createDate 2022-12-15 15:00:20
 */
@Service
@CacheConfig(cacheNames = "resource")
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourcePO> implements ResourceService {

    @Cacheable(key = "#method+' '+#uri")
    @Override
    public ResourcePO getResourceByUriAndMethod(String uri, String method) {
        LambdaQueryWrapper<ResourcePO> queryWrapper = new LambdaQueryWrapper<ResourcePO>()
                .select(ResourcePO::getId, ResourcePO::getIsAnonymous)
                .eq(ResourcePO::getUri, uri)
                .eq(ResourcePO::getRequestMethod, method);
        return getOne(queryWrapper);
    }
}




