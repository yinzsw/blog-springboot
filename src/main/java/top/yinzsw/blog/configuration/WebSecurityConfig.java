package top.yinzsw.blog.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;


/**
 * Security配置类
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AccessDecisionManager accessDecisionManager;
    private final FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs")
                .antMatchers("/doc.html", "/webjars/**")
                .antMatchers("/druid/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().maximumSessions(2000).sessionRegistry(new SessionRegistryImpl());

        http.authorizeRequests().withObjectPostProcessor(objectPostProcessor()).anyRequest().permitAll();

        //登录
        http.formLogin().loginProcessingUrl("/auth/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        //登出
        http.logout().logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler);

        //异常处理
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        return http.build();
    }

    private ObjectPostProcessor<FilterSecurityInterceptor> objectPostProcessor() {
        return new ObjectPostProcessor<>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                fsi.setAccessDecisionManager(accessDecisionManager);
                fsi.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                return fsi;
            }
        };
    }
}
