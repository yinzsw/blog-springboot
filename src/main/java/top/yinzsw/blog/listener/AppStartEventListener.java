package top.yinzsw.blog.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.RedisManager;

/**
 * 监听SpringBoot启动
 *
 * @author yinzsW
 * @since 23/01/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppStartEventListener implements ApplicationListener<ApplicationStartedEvent> {
    private final RedisManager redisManager;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("网站配置文件加载中...");
        redisManager.initWebSiteConfig();
        log.info("网站配置文件加载完毕");
    }
}
