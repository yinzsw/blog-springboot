package top.yinzsw.blog.manager;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/01/15
 */
@Slf4j
@SpringBootTest
public class RedisManagerTests {
    private @Autowired RedisManager redisManager;

    @Test
    void articleTest() {
        redisManager.getArticleLikeCount(List.of(1L, 2L));
    }
}
