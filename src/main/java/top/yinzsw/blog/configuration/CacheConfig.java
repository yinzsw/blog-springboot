package top.yinzsw.blog.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Configuration
@EnableCaching
public class CacheConfig {
    //TODO 用redis 实现service层缓存(用Kryo序列化对象)
}
