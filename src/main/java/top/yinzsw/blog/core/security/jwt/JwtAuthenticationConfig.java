package top.yinzsw.blog.core.security.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.security.Key;
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
     * 用户刷新token的Uri 标识
     */
    private String refreshUri;

    /**
     * 不需要认证的资源
     */
    private List<String> excludeUris;

    public boolean isRefreshUri(HttpMethod method, String uri) {
        return method.equals(HttpMethod.PUT) && refreshUri.equalsIgnoreCase(uri);
    }

    public Key getSecurityKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}
