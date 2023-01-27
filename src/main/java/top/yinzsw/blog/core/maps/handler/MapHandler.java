package top.yinzsw.blog.core.maps.handler;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 映射数据处理器
 *
 * @author yinzsW
 * @since 23/01/26
 */

public interface MapHandler<O, C> {

    /**
     * 获取源信息列表
     *
     * @return 数据源
     */
    List<O> getOriginList();

    /**
     * 获取映射信息传输对象
     *
     * @return 映射信息
     */
    C getContextDTO();

    /**
     * 映射一个新模型
     *
     * @param mappingFn 映射策略
     * @param <R>       返回值类型
     * @return 返回映射后的模型
     */
    default <R> R mappingOne(BiFunction<O, C, R> mappingFn) {
        List<O> originList = getOriginList();
        C contextDTO = getContextDTO();
        O origin = Objects.nonNull(originList) && originList.size() >= 1 ? originList.get(0) : null;
        return mappingFn.apply(origin, contextDTO);
    }

    /**
     * 映射一个新模型列表
     *
     * @param mappingFn 映射策略
     * @param <R>       返回值类型
     * @return 返回映射后的模型
     */
    default <R> R mappingList(BiFunction<List<O>, C, R> mappingFn) {
        return mappingFn.apply(getOriginList(), getContextDTO());
    }
}
