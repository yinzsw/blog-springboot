package top.yinzsw.blog.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * lambda 映射表操作
 *
 * @author yinzsW
 * @since 23/01/26
 */

public class MapQueryUtils<M, K> {
    private final SFunction<M, K> keyFn;
    private final List<?> keys;
    private final LambdaQueryWrapper<M> queryWrapper;

    private MapQueryUtils(SFunction<M, K> keyFn, List<?> keys) {
        this.keyFn = keyFn;
        this.keys = keys;
        this.queryWrapper = Wrappers.<M>lambdaQuery().in(keyFn, keys);
    }

    public static <M, K> MapQueryUtils<M, K> create(SFunction<M, K> keyFn, List<?> keys) {
        return new MapQueryUtils<>(keyFn, keys);
    }

    public MapQueryUtils<M, K> queryWrapper(Function<LambdaQueryWrapper<M>, LambdaQueryWrapper<M>> wrapperFn) {
        wrapperFn.apply(queryWrapper);
        return this;
    }

    public <V> List<V> getValues(SFunction<M, V> valueFn) {
        List<V> values = SimpleQuery.list(queryWrapper.select(valueFn), valueFn);
        return keys.size() > 1 ? values.stream().distinct().collect(Collectors.toList()) : values;
    }

    public Map<K, M> getKeyMap() {
        return SimpleQuery.keyMap(queryWrapper, keyFn);
    }

    public <V> Map<K, V> getKeyValueMap(SFunction<M, V> valueFn) {
        return SimpleQuery.map(queryWrapper.select(keyFn, valueFn), keyFn, valueFn);
    }

    public Map<K, List<M>> getGroupMap() {
        return SimpleQuery.group(queryWrapper, keyFn);
    }

    public <V> Map<K, List<V>> getGroupValueMap(SFunction<M, V> valueFn) {
        return SimpleQuery.group(queryWrapper.select(keyFn, valueFn), keyFn, Collectors.mapping(valueFn, Collectors.toList()));
    }
}
