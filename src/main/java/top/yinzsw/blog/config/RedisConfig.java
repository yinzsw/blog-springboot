package top.yinzsw.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.yinzsw.blog.extension.redis.serializer.ProtostuffRedisSerializer;


/**
 * redis配置
 *
 * @author yinzsW
 * @since 22/12/15
 **/
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final LettuceConnectionFactory redisConnectionFactory;
    private final ProtostuffRedisSerializer protostuffRedisSerializer;


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        var stringRedisSerializer = new StringRedisSerializer();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(protostuffRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(protostuffRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}