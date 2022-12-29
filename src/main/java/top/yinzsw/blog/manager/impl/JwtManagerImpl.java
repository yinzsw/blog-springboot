package top.yinzsw.blog.manager.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.HttpContext;
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
@Component
@RequiredArgsConstructor
public class JwtManagerImpl implements JwtManager {
    private final static String X_CLAIM = "xcm";
    @Value("${jwt.key}")
    private String jwtKey;
    private final HttpContext httpContext;

    @Override
    public TokenVO createTokenVO(Long userId, List<String> roles) {
        String sign = getSign();
        long currentTimeMillis = System.currentTimeMillis();
        Key jwk = Keys.hmacShaKeyFor(jwtKey.getBytes());

        Function<TokenTypeEnum, String> generateToken = tokenTypeEnum -> Jwts.builder()
                .claim(X_CLAIM, ClaimsDTO.builder().uid(userId).roles(roles).sign(sign).type(tokenTypeEnum).build())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + tokenTypeEnum.getTtl()))
                .signWith(jwk)
                .compact();

        String accessToken = generateToken.apply(TokenTypeEnum.ACCESS);
        String refreshToken = generateToken.apply(TokenTypeEnum.REFRESH);
        return new TokenVO(accessToken, refreshToken);
    }

    private String getSign() {
        String userAgent = Optional.ofNullable(httpContext.getUserAgent()).orElse("IP");
        String userIpAddress = Optional.ofNullable(httpContext.getUserIpAddress()).orElse("UA");

        byte[] dataBytes = String.join("", userAgent.concat(userIpAddress)).getBytes();
        byte[] keyBytes = Keys.hmacShaKeyFor(jwtKey.getBytes()).getEncoded();
        byte[] originBytes = ByteUtils.concat(dataBytes, keyBytes);

        Arrays.sort(originBytes);
        return DigestUtils.md5DigestAsHex(originBytes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ClaimsDTO parseTokenInfo(String token, TokenTypeEnum expectTokenType) throws BizException {
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
