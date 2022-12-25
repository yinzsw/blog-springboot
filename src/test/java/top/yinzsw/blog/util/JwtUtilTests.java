package top.yinzsw.blog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.security.Key;

/**
 * desc
 *
 * @author yinzsW
 * @since 22/12/23
 */
@SpringBootTest
public class JwtUtilTests {

    @Value("${jwt.key}")
    private String key;

    @Test
    void jwtTest() {
        Key secretKey = Keys.hmacShaKeyFor(key.getBytes());
        String token = Jwts.builder().setSubject("NB").claim("uid", 1).signWith(secretKey).compact();

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        Claims body = jwtParser.parseClaimsJws(token).getBody();

    }
}
