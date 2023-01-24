package top.yinzsw.blog.core.maps;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 简单映射数据提供器
 *
 * @author yinzsW
 * @since 23/01/20
 */

public class SimpleMapProvider<M, K> implements MapProvider<M, K> {
    private final SFunction<M, K> keyFn;
    private final Collection<? extends Serializable> keys;

    public SimpleMapProvider(SFunction<M, K> keyFn,
                             Collection<? extends Serializable> keys) {
        this.keyFn = keyFn;
        this.keys = keys;
    }

    @Override
    @SafeVarargs
    public final Map<K, M> getKeyMap(SFunction<M, ?>... columns) {
        return SimpleQuery.keyMap(Wrappers.<M>lambdaQuery().select(columns).in(keyFn, keys), keyFn);
    }

    @Override
    public final <V> Map<K, V> getKeyValueMap(SFunction<M, V> valueFn) {
        return SimpleQuery.map(Wrappers.<M>lambdaQuery().select(keyFn, valueFn).in(keyFn, keys), keyFn, valueFn);
    }

    @Override
    @SafeVarargs
    public final Map<K, List<M>> getGroupMap(SFunction<M, ?>... columns) {
        return SimpleQuery.group(Wrappers.<M>lambdaQuery().select(columns).in(keyFn, keys), keyFn);
    }

    @Override
    public final <V> Map<K, List<V>> getGroupValueMap(SFunction<M, V> valueFn) {
        LambdaQueryWrapper<M> queryWrapper = Wrappers.<M>lambdaQuery().select(keyFn, valueFn).in(keyFn, keys);
        return SimpleQuery.group(queryWrapper, keyFn, Collectors.mapping(valueFn, Collectors.toList()));
    }

    @Override
    public final <V> List<V> getValues(SFunction<M, V> valueFn) {
        return getValues(valueFn, Function.identity());
    }

    @Override
    public final <V> List<V> getValues(SFunction<M, V> valueFn,
                                       Function<LambdaQueryWrapper<M>, LambdaQueryWrapper<M>> extraFn) {
        List<V> values = SimpleQuery.list(extraFn.apply(Wrappers.<M>lambdaQuery().select(valueFn).in(keyFn, keys)), valueFn);
        return values.size() > 1 ? values.stream().distinct().collect(Collectors.toList()) : values;
    }
}
