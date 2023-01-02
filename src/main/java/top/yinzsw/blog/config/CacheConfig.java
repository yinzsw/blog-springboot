package top.yinzsw.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import top.yinzsw.blog.constant.RedisConst;
import top.yinzsw.blog.extension.redis.serializer.ProtostuffRedisSerializer;

import java.time.Duration;

/**
 * 缓存配置
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {
    private final LettuceConnectionFactory redisConnectionFactory;
    private final ProtostuffRedisSerializer protostuffRedisSerializer;

    @Bean
    public CacheManager redisCacheConfiguration() {
        RedisCacheWriter writer = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(3))
                .computePrefixWith(cacheName -> RedisConst.CACHE_PREFIX + cacheName + ":")
                .serializeValuesWith(SerializationPair.fromSerializer(protostuffRedisSerializer));
        return new RedisCacheManager(writer, configuration);
    }
}
