package top.yinzsw.blog.configuration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson Json 配置类
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Configuration
@AllArgsConstructor
public class JacksonConfig {
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}
