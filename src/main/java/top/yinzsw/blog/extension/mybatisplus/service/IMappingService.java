package top.yinzsw.blog.extension.mybatisplus.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 处理映射表的Service
 *
 * @author yinzsW
 * @since 23/01/14
 */

public interface IMappingService<T> extends IService<T> {

    /**
     * 根据键查询值列表
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param key     键值
     * @param <K>     键类型
     * @param <V>     值类型
     * @return 映射值列表
     */
    <K, V> List<V> mappingList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, K key);

    /**
     * 根据键查询值列表
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param keys    键值列表
     * @param <K>     键类型
     * @param <V>     值类型
     * @return 映射值列表
     */
    <K, V> List<V> mappingList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys);

    /**
     * 根据键查询键值映射
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param keys    键值列表
     * @param <K>     键类型
     * @param <V>     值类型
     * @return 映射
     */
    <K, V> Map<K, List<V>> mappingMap(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys);

    /**
     * 根据键查询键值映射
     *
     * @param keyFn   键策略
     * @param valueFn 值策略
     * @param keys    键值列表
     * @param <K>     键类型
     * @param <V>     值类型
     * @return 映射
     */
    <K, V> Map<K, List<T>> mappingMapObj(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys);
}
