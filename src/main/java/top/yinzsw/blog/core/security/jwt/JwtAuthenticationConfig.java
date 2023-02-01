package top.yinzsw.blog.core.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * jwt配置类
 *
 * @author yinzsW
 * @since 23/01/31
 */
@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtAuthenticationConfig {

    /**
     * JWT密钥
     */
    private String key;

    /**
     * JWT中存放自定义信息的字段名
     */
    private String XClaimName;

    /**
     * 存放token的RequestHeaderName
     */
    private String tokenName = "token";

    /**
     * 用户刷新token的Uri
     */
    private String refreshUri;

    /**
     * 不需要认证的资源
     */
    private List<String> excludeUris;
}
