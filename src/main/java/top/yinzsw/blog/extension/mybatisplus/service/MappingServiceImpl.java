package top.yinzsw.blog.extension.mybatisplus.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处理映射表的Service 实现
 *
 * @author yinzsW
 * @since 23/01/14
 */

public class MappingServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IMappingService<T> {

    @Override
    public <K, V> List<V> mappingList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, K key) {
        return mappingList(keyFn, valueFn, List.of(key));
    }

    @Override
    public <K, V> List<V> mappingList(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.list(Wrappers.<T>lambdaQuery().select(valueFn).in(keyFn, keys), valueFn)
                .stream().distinct().collect(Collectors.toList());
    }

    @Override
    public <K, V> Map<K, List<V>> mappingMap(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.group(Wrappers.<T>lambdaQuery().in(keyFn, keys),
                keyFn, Collectors.mapping(valueFn, Collectors.toList()));
    }

    @Override
    public <K, V> Map<K, List<T>> mappingMapObj(SFunction<T, K> keyFn, SFunction<T, V> valueFn, Collection<K> keys) {
        return SimpleQuery.group(Wrappers.<T>lambdaQuery().in(keyFn, keys), keyFn);
    }
}
