package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.extension.mybatisplus.SqlUtils;
import top.yinzsw.blog.manager.WebConfigManager;
import top.yinzsw.blog.model.po.WebsiteConfigPO;

import java.util.Map;

/**
 * 网站配置信息通用业务层实现
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Service
@RequiredArgsConstructor
public class WebConfigManagerImpl implements WebConfigManager {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void initWebSiteConfig() {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(WEBSITE_CONFIG))) {
            return;
        }
        Map<String, Object> config = objectMapper.convertValue(new WebsiteConfigPO(), new TypeReference<>() {
        });
        redisTemplate.<String, Object>opsForHash().putAll(WEBSITE_CONFIG, config);
    }

    @Override
    public WebsiteConfigPO getWebSiteConfig() {
        Map<String, Object> websiteConfigMap = redisTemplate.<String, Object>opsForHash().entries(WEBSITE_CONFIG);
        return objectMapper.convertValue(websiteConfigMap, WebsiteConfigPO.class);
    }

    public <T, R> R getWebSiteConfig(SFunction<T, R> sFunction) {
        String propertyName = SqlUtils.getPropertyName(sFunction);
        return redisTemplate.<String, R>opsForHash().get(WEBSITE_CONFIG, propertyName);
    }
}
