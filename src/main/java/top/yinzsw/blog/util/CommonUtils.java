package top.yinzsw.blog.util;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用工具类
 *
 * @author yinzsW
 * @since 23/01/23
 */

public class CommonUtils {

    /**
     * 集合元素类型转换
     *
     * @param collection 集合
     * @param function   元素转换策略
     * @param <T>        原始集合元素类型
     * @param <R>        转换后的集合元素类型
     * @return 新的元素类型的列表
     */
    public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> function) {
        return CollectionUtils.isEmpty(collection) ?
                Collections.emptyList() :
                collection.stream().map(function).collect(Collectors.toList());
    }
}
