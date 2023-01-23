package top.yinzsw.blog.core.maps;

import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 抽象映射构建器
 *
 * @author yinzsW
 * @since 23/01/21
 */

public abstract class AbstractMapsBuilder<O, C> implements MapsBuilder<O, C> {
    /**
     * 任务列表
     */
    private final List<Runnable> runnableList = new ArrayList<>();

    /**
     * 源数据列表
     */
    private final List<O> originList;

    /**
     * 封装数据的上下文对象
     */
    protected final C context;

    protected AbstractMapsBuilder(List<O> originList, C context) {
        this.originList = originList;
        this.context = context;
    }

    @Override
    public <R> R mappingOne(BiFunction<O, C, R> mappingFn) {
        O origin = Objects.nonNull(originList) && originList.size() >= 1 ? originList.get(0) : null;
        return mappingFn.apply(origin, context);
    }

    @Override
    public <R> R mappingList(BiFunction<List<O>, C, R> mappingFn) {
        return mappingFn.apply(originList, context);
    }

    /**
     * 获取id列表
     *
     * @param idFn id策略
     * @param ids  源id列表
     * @param <T>  id类型
     * @return 新id列表
     */
    @SafeVarargs
    protected final <T> List<T> getIds(Function<O, T> idFn, T... ids) {
        return ObjectUtils.isEmpty(ids) ? originList.stream().map(idFn).collect(Collectors.toList()) : List.of(ids);
    }

    /**
     * 添加任务
     *
     * @param runnable 任务
     */
    protected final void addRunnable(Runnable runnable) {
        runnableList.add(runnable);
    }

    /**
     * 并行构建
     *
     * @return 构建器接口
     */
    public final MapsBuilder<O, C> parallelBuild() {
        Executor executor = getExecutor();
        runnableList.stream()
                .map(runnable -> Objects.isNull(executor) ? CompletableFuture.runAsync(runnable) : CompletableFuture.runAsync(runnable, executor))
                .forEach(CompletableFuture::join);
        runnableList.clear();
        return this;
    }

    /**
     * 串行构建
     *
     * @return 构建器接口
     */
    public final MapsBuilder<O, C> serialBuild() {
        runnableList.forEach(Runnable::run);
        runnableList.clear();
        return this;
    }

    /**
     * 获取线程池
     *
     * @return 线程池
     */
    protected abstract Executor getExecutor();
}
