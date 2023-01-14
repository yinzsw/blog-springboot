package top.yinzsw.blog.util;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/01/14
 */

public class MybatisPlusUtils {
    public static <T, K, V> List<V> mappingList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, K key) {
        return SimpleQuery.list(Wrappers.<T>lambdaQuery().select(valueFn).in(keyFn, key), valueFn);
    }

    public static <T, K, V> List<V> mappingDistinctList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.list(Wrappers.<T>lambdaQuery().select(valueFn).in(keyFn, keys), valueFn)
                .stream().distinct().collect(Collectors.toList());
    }

    public static <T, K> Map<K, List<T>> mappingGroup(SFunction<T, K> keyFn, Collection<K> keys) {
        return SimpleQuery.group(Wrappers.<T>lambdaQuery().in(keyFn, keys), keyFn);
    }

    public static <T, K, V> Map<K, List<V>> mappingGroup(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.group(Wrappers.<T>lambdaQuery()
                .select(keyFn, valueFn)
                .in(keyFn, keys), keyFn, Collectors.mapping(valueFn, Collectors.toList()));
    }

    public static <T, K> Map<K, T> mappingMap(SFunction<T, K> keyFn, Collection<K> keys) {
        return SimpleQuery.keyMap(Wrappers.<T>lambdaQuery().in(keyFn, keys), keyFn);
    }

    public static <T, K, V> Map<K, V> mappingMap(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.map(Wrappers.<T>lambdaQuery()
                .select(keyFn, valueFn)
                .in(keyFn, keys), keyFn, valueFn);
    }
}
