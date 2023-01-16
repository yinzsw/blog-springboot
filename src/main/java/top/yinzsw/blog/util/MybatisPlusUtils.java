package top.yinzsw.blog.util;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * mybatis plus 工具类
 *
 * @author yinzsW
 * @since 23/01/14
 */

public class MybatisPlusUtils {

    /**
     * 根据键查找值列表
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param key     键的值
     * @param <T>     映射表
     * @param <K>     映射表键
     * @param <V>     映射表值
     * @return 值列表
     */
    public static <T, K, V> List<V> mappingList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, K key) {
        return SimpleQuery.list(Wrappers.<T>lambdaQuery().select(valueFn).in(keyFn, key), valueFn);
    }

    /**
     * 根据键列表查找值列表(去重)(多对多)
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param keys    键的值列表
     * @param <T>     映射表
     * @param <K>     映射表键
     * @param <V>     映射表值
     * @return 去重后的值列表
     */
    public static <T, K, V> List<V> mappingDistinctList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.list(Wrappers.<T>lambdaQuery().select(valueFn).in(keyFn, keys), valueFn)
                .stream().distinct().collect(Collectors.toList());
    }

    /**
     * 根据键查找并生成Map类型的映射表(多对多)
     *
     * @param keyFn 键策略
     * @param keys  键的值列表
     * @param <T>   映射表
     * @param <K>   映射表键
     * @return Map映射表(< K > = < T >)
     */
    public static <T, K> Map<K, List<T>> mappingGroup(SFunction<T, K> keyFn, Collection<K> keys) {
        return SimpleQuery.group(Wrappers.<T>lambdaQuery().in(keyFn, keys), keyFn);
    }

    /**
     * 根据键查找并生成Map类型的映射表(多对多)
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param keys    键的值列表
     * @param <T>     映射表
     * @param <K>     映射表键
     * @param <V>     映射表值
     * @return Map映射表(< K > = List < V >)
     */
    public static <T, K, V> Map<K, List<V>> mappingGroup(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.group(Wrappers.<T>lambdaQuery()
                .select(keyFn, valueFn)
                .in(keyFn, keys), keyFn, Collectors.mapping(valueFn, Collectors.toList()));
    }

    /**
     * 根据键查找并生成Map类型的映射表(一对一)
     *
     * @param keyFn 键策略
     * @param keys  键的值列表
     * @param <T>   映射表
     * @param <K>   映射表键
     * @return Map映射表(< K > = < T >)
     */
    public static <T, K> Map<K, T> mappingMap(SFunction<T, K> keyFn, Collection<K> keys) {
        return SimpleQuery.keyMap(Wrappers.<T>lambdaQuery().in(keyFn, keys), keyFn);
    }

    /**
     * 根据键查找并生成Map类型的映射表(一对一)
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param keys    键的值列表
     * @param <T>     映射表
     * @param <K>     映射表键
     * @param <V>     映射表值
     * @return Map映射表(< K > = < V >)
     */
    public static <T, K, V> Map<K, V> mappingMap(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.map(Wrappers.<T>lambdaQuery()
                .select(keyFn, valueFn)
                .in(keyFn, keys), keyFn, valueFn);
    }

    /**
     * 获取JavaBean属性名
     *
     * @param sFunction 返回调用get方法的lambda表达式
     * @param <T>       源类型
     * @param <R>       返回类型
     * @return 属性名
     */
    public static <T, R> String getPropertyName(SFunction<T, R> sFunction) {
        return PropertyNamer.methodToProperty(LambdaUtils.extract(sFunction).getImplMethodName());
    }
}
