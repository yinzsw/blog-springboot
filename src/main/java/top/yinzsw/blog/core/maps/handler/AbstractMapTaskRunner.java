package top.yinzsw.blog.core.maps.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 抽象映射任务执行器
 *
 * @author yinzsW
 * @since 23/01/27
 **/
public abstract class AbstractMapTaskRunner<O, C> implements MapHandler<O, C> {

    /**
     * 任务列表
     */
    private final List<Runnable> runnableTasks = new ArrayList<>();

    /**
     * 添加任务
     *
     * @param runnableList 任务
     */
    protected final void addRunnable(Runnable... runnableList) {
        runnableTasks.addAll(Arrays.asList(runnableList));
    }

    /**
     * 并行运行
     */
    public final MapHandler<O, C> parallelRun() {
        Executor executor = getExecutor();
        runnableTasks.stream()
                .map(runnable -> Objects.isNull(executor) ?
                        CompletableFuture.runAsync(runnable) :
                        CompletableFuture.runAsync(runnable, executor))
                .forEach(CompletableFuture::join);
        runnableTasks.clear();
        return this;
    }

    /**
     * 串行运行
     */
    public final MapHandler<O, C> serialRun() {
        runnableTasks.forEach(Runnable::run);
        runnableTasks.clear();
        return this;
    }

    /**
     * 获取线程池
     *
     * @return 线程池
     */
    protected abstract Executor getExecutor();
}
