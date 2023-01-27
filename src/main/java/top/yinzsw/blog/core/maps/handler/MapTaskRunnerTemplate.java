package top.yinzsw.blog.core.maps.handler;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 映射任务执行器构建模板
 *
 * @author yinzsW
 * @since 23/01/27
 */

public class MapTaskRunnerTemplate<O, C> extends AbstractMapTaskRunner<O, C> {
    private final Executor executor;
    private final List<O> originList;
    private final C contextDTO;

    public MapTaskRunnerTemplate(Executor executor, List<O> originList, C contextDTO) {
        this.executor = executor;
        this.originList = originList;
        this.contextDTO = contextDTO;
    }

    @Override
    protected Executor getExecutor() {
        return executor;
    }

    @Override
    public List<O> getOriginList() {
        return originList;
    }

    @Override
    public C getContextDTO() {
        return contextDTO;
    }
}
