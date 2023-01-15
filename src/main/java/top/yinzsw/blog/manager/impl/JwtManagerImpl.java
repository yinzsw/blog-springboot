package top.yinzsw.blog.manager.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.JwtManager;
import top.yinzsw.blog.model.dto.ClaimsDTO;
import top.yinzsw.blog.model.vo.TokenVO;

import java.security.Key;
import java.util.*;
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
    private final static String X_CLAIM = "xcm";
    private final static String UNKNOWN = "unknown";
    private @Value("${jwt-key}") String jwtKey;
    private final HttpContext httpContext;

    @Override
    public TokenVO createTokenVO(Long userId, List<String> roles) {
        String sign = getSign();
        long currentTimeMillis = System.currentTimeMillis();
        Key jwk = Keys.hmacShaKeyFor(jwtKey.getBytes());

        Function<TokenTypeEnum, String> generateToken = tokenTypeEnum -> Jwts.builder()
                .claim(X_CLAIM, new ClaimsDTO().setUid(userId).setRoles(roles).setSign(sign).setType(tokenTypeEnum))
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + tokenTypeEnum.getTtl()))
                .signWith(jwk)
                .compact();

        String accessToken = generateToken.apply(TokenTypeEnum.ACCESS);
        String refreshToken = generateToken.apply(TokenTypeEnum.REFRESH);
        return new TokenVO(accessToken, refreshToken);
    }

    private String getSign() {
        String userAgent = Optional.ofNullable(httpContext.getUserAgent()).orElse(UNKNOWN);
        String userIpAddress = Optional.ofNullable(httpContext.getUserIpAddress()).orElse(UNKNOWN);

        byte[] dataBytes = String.join("", userAgent.concat(userIpAddress)).getBytes();
        byte[] keyBytes = Keys.hmacShaKeyFor(jwtKey.getBytes()).getEncoded();
        byte[] originBytes = ByteUtils.concat(dataBytes, keyBytes);

        Arrays.sort(originBytes);
        return DigestUtils.md5DigestAsHex(originBytes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ClaimsDTO parseTokenInfo(String token, TokenTypeEnum expectTokenType) throws BizException {
        if (!StringUtils.hasText(token)) {
            throw new BizException(ResponseCodeEnum.TOKEN_ERROR, "token不能为空");
        }

        Key jwk = Keys.hmacShaKeyFor(jwtKey.getBytes());
        JwtParser jwtParser = Jwts.parserBuilder()
                .deserializeJsonWith(new JacksonDeserializer(Maps.of(X_CLAIM, ClaimsDTO.class).build()))
                .setSigningKey(jwk).build();

        Jws<Claims> claimsJws;
        try {
            claimsJws = jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new BizException(ResponseCodeEnum.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new BizException(ResponseCodeEnum.TOKEN_ERROR, "token解析失败");
        }

        Claims claims = claimsJws.getBody();
        ClaimsDTO claimsDTO = claims.get(X_CLAIM, ClaimsDTO.class);

        if (!getSign().equals(claimsDTO.getSign())) {
            throw new BizException(ResponseCodeEnum.TOKEN_ERROR, "非法的token解析");
        }

        if (Objects.isNull(expectTokenType) || !expectTokenType.equals(claimsDTO.getType())) {
            throw new BizException(ResponseCodeEnum.TOKEN_ERROR, "不是期待的token类型");
        }
        return claimsDTO;
    }
}
