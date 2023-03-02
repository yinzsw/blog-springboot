package top.yinzsw.blog.core.security.jwt;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class JwtManagerImplTest {

    @Test
    void createTokenVO() {

        log.info("{}", IdWorker.getId());
    }
}