package top.yinzsw.blog.core.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.model.vo.TokenVO;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    private final JwtAuthenticationConfig jwtAuthenticationConfig;

    @Override
    public TokenVO createTokenVO(Long userId, List<String> roles) {
        String sign = getSign();
        long currentTimeMillis = System.currentTimeMillis();
        Key jwk = Keys.hmacShaKeyFor(jwtAuthenticationConfig.getKey().getBytes());

        Function<TokenTypeEnum, String> generateToken = tokenTypeEnum -> Jwts.builder()
                .claim(jwtAuthenticationConfig.getXClaimName(), new JwtContextDTO()
                        .setUid(userId).setRoles(roles).setSign(sign).setType(tokenTypeEnum))
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + tokenTypeEnum.getTtl()))
                .signWith(jwk)
                .compact();

        String accessToken = generateToken.apply(TokenTypeEnum.ACCESS);
        String refreshToken = generateToken.apply(TokenTypeEnum.REFRESH);
        return new TokenVO(accessToken, refreshToken);
    }

    /**
     * UA+IP+jwtKey 生成新的sign
     *
     * @return sign
     */
    private String getSign() {
        String userAgent = httpContext.getUserAgent();
        String userIpAddress = httpContext.getUserIpAddress().orElse(userAgent.length() + "");

        byte[] dataBytes = String.join("", userAgent.concat(userIpAddress)).getBytes();
        byte[] keyBytes = Keys.hmacShaKeyFor(jwtAuthenticationConfig.getKey().getBytes()).getEncoded();
        byte[] originBytes = ByteUtils.concat(dataBytes, keyBytes);

        Arrays.sort(originBytes);
        return DigestUtils.md5DigestAsHex(originBytes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public JwtContextDTO parseAndGetJwtContext(String token, boolean isRefresh) throws AuthenticationException {
        if (!StringUtils.hasText(token)) {
            throw new BadCredentialsException("无效的访问令牌");
        }

        Key jwk = Keys.hmacShaKeyFor(jwtAuthenticationConfig.getKey().getBytes());
        JacksonDeserializer deserializer = new JacksonDeserializer(Maps.of(jwtAuthenticationConfig.getXClaimName(), JwtContextDTO.class).build());
        JwtParser jwtParser = Jwts.parserBuilder().deserializeJsonWith(deserializer).setSigningKey(jwk).build();

        Jws<Claims> claimsJws;
        try {
            claimsJws = jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("访问令牌已过期");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new BadCredentialsException("无效的访问令牌");
        }

        Claims claims = claimsJws.getBody();
        JwtContextDTO jwtContextDTO = claims.get(jwtAuthenticationConfig.getXClaimName(), JwtContextDTO.class);
        if (!getSign().equals(jwtContextDTO.getSign())) {
            throw new BadCredentialsException("非法的访问令牌");
        }

        TokenTypeEnum tokenType = isRefresh ? TokenTypeEnum.REFRESH : TokenTypeEnum.ACCESS;
        if (!tokenType.equals(jwtContextDTO.getType())) {
            throw new BadCredentialsException("访问令牌类型不正确");
        }
        return jwtContextDTO;
    }
}
