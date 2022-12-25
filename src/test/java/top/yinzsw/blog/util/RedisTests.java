package top.yinzsw.blog.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.model.request.LoginRequest;

/**
 * desc
 *
 * @author yinzsW
 * @since 22/12/22
 */
@SpringBootTest
public class RedisTests {
    @Autowired
    RedisUtil redisUtil;


    @Test
    void addTest() {
        LoginRequest loginRequest = new LoginRequest("add", "123456");
        redisUtil.set("blog:login", loginRequest);
//        LoginRequest o = redisUtil.get("blog:login");
//        System.out.println(o);
    }
}
