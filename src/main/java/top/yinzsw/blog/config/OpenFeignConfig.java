package top.yinzsw.blog.config;

import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign配置类
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Configuration
@EnableFeignClients(value = "top.yinzsw.blog.client", defaultConfiguration = OpenFeignConfig.class)
public class OpenFeignConfig {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 1000, 3);
    }
}
