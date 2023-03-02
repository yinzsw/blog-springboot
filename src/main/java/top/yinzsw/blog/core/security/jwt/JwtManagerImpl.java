package top.yinzsw.blog.core.security.jwt;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.model.vo.TokenVO;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Json Web Token 操作实现
 *
 * @author yinzsW
 * @since 22/12/21
 */
@Service
@RequiredArgsConstructor
public class JwtManagerImpl implements JwtManager {
    private final HttpContext httpContext;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final JwtAuthenticationConfig jwtAuthenticationConfig;

    /**
     * UA+IP+jwtKey 生成新的sign
     *
     * @param keyBytes jwtKey byte array
     * @return sign
     */
    private String getSign(byte[] keyBytes) {
        String userAgent = httpContext.getUserAgent();
        String userIpAddress = httpContext.getUserIpAddress().orElse(userAgent.length() + "");

        byte[] dataBytes = String.join("", userAgent.concat(userIpAddress)).getBytes();
        byte[] originBytes = ByteUtils.concat(dataBytes, keyBytes);

        Arrays.sort(originBytes);
        return DigestUtils.md5DigestAsHex(originBytes);
    }

    @Override
    public TokenVO createTokenVO(Long userId, List<Long> roles) {
        Key jwk = jwtAuthenticationConfig.getSecurityKey();
        JwtContextDTO jwtContextDTO = new JwtContextDTO().setUid(userId).setRoles(roles).setSign(getSign(jwk.getEncoded()));

        JacksonSerializer<Map<String, ?>> serializer = new JacksonSerializer<>(objectMapper);
        Function<TokenTypeEnum, String> getTokenFn =
                tokenType -> Jwts.builder()
                        .setClaims(jwtContextDTO.setType(tokenType).toMap(objectMapper))
                        .serializeToJsonWith(serializer)
                        .signWith(jwk)
                        .compact();

        String accessToken = getTokenFn.apply(TokenTypeEnum.ACCESS);
        String refreshToken = getTokenFn.apply(TokenTypeEnum.REFRESH);
        return new TokenVO(accessToken, refreshToken);
    }

    @Override
    public JwtContextDTO parseAndGetJwtContext(boolean isRefresh) throws AuthenticationException {
        String token = httpContext.getBearerToken().orElseThrow(() -> new BadCredentialsException("无效的访问令牌"));

        Key jwk = jwtAuthenticationConfig.getSecurityKey();
        JacksonDeserializer<Map<String, ?>> serializer = new JacksonDeserializer<>(objectMapper);
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwk).deserializeJsonWith(serializer).build();

        Jws<Claims> claimsJws;
        try {
            claimsJws = jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("访问令牌已过期");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new BadCredentialsException("无效的访问令牌");
        }

        JwtContextDTO jwtContextDTO = objectMapper.convertValue(claimsJws.getBody(), JwtContextDTO.class);
        if (!getSign(jwk.getEncoded()).equals(jwtContextDTO.getSign())) {
            throw new BadCredentialsException("非法的访问令牌");
        }

        String blockTokenKey = RedisConstEnum.USER_BLOCK_TOKEN.getKey(jwtContextDTO.getVid());
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(blockTokenKey))) {
            throw new BadCredentialsException("访问令牌已失效");
        }

        TokenTypeEnum tokenType = isRefresh ? TokenTypeEnum.REFRESH : TokenTypeEnum.ACCESS;
        if (!tokenType.equals(jwtContextDTO.getType())) {
            throw new BadCredentialsException("访问令牌类型不正确");
        }

        return jwtContextDTO;
    }

    @Override
    public boolean blockUserToken() {
        var jwtContextDTO = JwtManager.getCurrentContextDTO()
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        String blockTokenKey = RedisConstEnum.USER_BLOCK_TOKEN.getKey(jwtContextDTO.getVid());

        //token剩余生命时间
        long currentTtl = jwtContextDTO.getExp() - (SystemClock.now() / 1000) + 3;
        stringRedisTemplate.opsForValue().set(blockTokenKey, "", Duration.ofSeconds(currentTtl));
        return true;
    }
}
