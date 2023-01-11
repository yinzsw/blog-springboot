package top.yinzsw.blog.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web mvc配置
 *
 * @author yinzsW
 * @since 2022/12/15
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final List<HandlerInterceptor> handlerInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        handlerInterceptors.forEach(registry::addInterceptor);
    }
}
