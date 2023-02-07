package top.yinzsw.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 *
 * @author yinzsW
 * @since 23/01/09
 **/
@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        int processorsNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize = processorsNum * 2 + 1;
        int maxPoolSize = corePoolSize * 10;
        int queueCapacity = maxPoolSize * 4;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("task-");
        executor.setKeepAliveSeconds(30);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}