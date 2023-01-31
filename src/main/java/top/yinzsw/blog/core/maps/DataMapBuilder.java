package top.yinzsw.blog.core.maps;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 数据映射构建器
 *
 * @author yinzsW
 * @since 23/01/30
 */
@Component
@RequiredArgsConstructor
public class DataMapBuilder {
    private final ThreadPoolTaskExecutor taskExecutor;

    public <C> Builder<C> builder(C mapsContext) {
        return new Builder<>(mapsContext);
    }

    @AllArgsConstructor
    public class Builder<C> {
        private final C mapsContext;
        private final List<Runnable> runnableTasks = new ArrayList<>();

        public <T extends Map<? extends Serializable, ?>> Builder<C> addMap(BiConsumer<C, T> consumer, Supplier<T> supplier) {
            runnableTasks.add(() -> consumer.accept(mapsContext, supplier.get()));
            return this;
        }

        public C build() {
            runnableTasks.stream()
                    .map(runnable -> CompletableFuture.runAsync(runnable, taskExecutor))
                    .forEach(CompletableFuture::join);
            return mapsContext;
        }
    }
}
