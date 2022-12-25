package top.yinzsw.blog.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson Json 配置类
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Configuration
public class JacksonConfig {
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}
