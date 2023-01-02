package top.yinzsw.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 其他功能的配置类
 *
 * @author yinzsW
 * @since 22/12/30
 */
@Configuration
@EnableAsync
public class OtherConfig {

    /**
     * 为Jackson {@link ObjectMapper} 添加新的时间类支持
     *
     * @return JavaTimeModule
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    /**
     * 开启 Bean Validator 快速失败机制
     *
     * @return 验证器
     */
    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }
}
