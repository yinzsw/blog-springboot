package top.yinzsw.blog.core.task;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 任务处理器
 *
 * @author yinzsW
 * @since 23/02/03
 */
@Component
@RequiredArgsConstructor
public class RunnableTaskHandler {
    private final ThreadPoolTaskExecutor taskExecutor;

    public Handler handler() {
        return new Handler();
    }

    public class Handler {
        private final List<Runnable> runnableTasks = new ArrayList<>();

        public Handler addTask(Runnable task) {
            return addTask(true, task);
        }

        public Handler addTask(boolean condition, Runnable task) {
            if (condition) {
                runnableTasks.add(task);
            }
            return this;
        }

        public void handle() {
            runnableTasks.stream()
                    .map(runnable -> CompletableFuture.runAsync(runnable, taskExecutor))
                    .forEach(CompletableFuture::join);
        }
    }
}
