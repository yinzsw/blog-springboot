package top.yinzsw.blog.configuration;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.yinzsw.blog.handler.PageableHandlerInterceptor;

/**
 * web mvc配置
 *
 * @author yinzsW
 * @since 2022/12/15
 */
@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final PageableHandlerInterceptor pageableHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageableHandlerInterceptor);
    }
}
