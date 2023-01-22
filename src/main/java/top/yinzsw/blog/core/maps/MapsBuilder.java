package top.yinzsw.blog.core.maps;

import java.util.List;
import java.util.function.BiFunction;

/**
 * 映射构建器接口
 *
 * @author yinzsW
 * @since 23/01/22
 */

public interface MapsBuilder<O, C> {

    /**
     * 映射一个新模型
     *
     * @param mappingFn 映射策略
     * @param <R>       返回值类型
     * @return 返回映射后的模型
     */
    <R> R mappingOne(BiFunction<O, C, R> mappingFn);

    /**
     * 映射一个新模型列表
     *
     * @param mappingFn 映射策略
     * @param <R>       返回值类型
     * @return 返回映射后的模型
     */
    <R> R mappingList(BiFunction<List<O>, C, R> mappingFn);
}
