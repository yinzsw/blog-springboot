package top.yinzsw.blog.core.maps;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 映射数据提供器
 *
 * @author yinzsW
 * @since 23/01/20
 */
@SuppressWarnings("all")
public interface MapProvider<M, K> {

    /**
     * 获取映射关系
     *
     * @param columns 查询的列
     * @return [key->对象]映射关系
     */
    Map<K, M> getKeyMap(SFunction<M, ?>... columns);

    /**
     * 获取映射关系
     *
     * @param valueFn 值策略
     * @param <V>     值类型
     * @return [key->value]映射关系
     */
    <V> Map<K, V> getKeyValueMap(SFunction<M, V> valueFn);

    /**
     * 获取映射关系
     *
     * @param columns 查询的列
     * @return [key->对象列表]映射关系
     */
    Map<K, List<M>> getGroupMap(SFunction<M, ?>... columns);

    /**
     * 获取映射关系
     *
     * @param valueFn 值策略
     * @param <V>     值类型
     * @return [key->值列表]映射关系
     */
    <V> Map<K, List<V>> getGroupValueMap(SFunction<M, V> valueFn);

    /**
     * 获取值列表
     *
     * @param valueFn 值策略
     * @param <V>     值类型
     * @return 值列表
     */
    <V> List<V> getValues(SFunction<M, V> valueFn);

    /**
     * 获取值列表
     *
     * @param valueFn 值策略
     * @param extraFn 额外条件
     * @param <V>     值类型
     * @return 值列表
     */
    <V> List<V> getValues(SFunction<M, V> valueFn, Function<LambdaQueryWrapper<M>, LambdaQueryWrapper<M>> extraFn);

    /**
     * 根据setter function 获取对象属性名
     *
     * @param sFunction getter
     * @param <T>       JavaBean类型
     * @param <R>       返回数据类型
     * @return 属性名
     */
    static <T, R> String getPropertyName(SFunction<T, R> sFunction) {
        return PropertyNamer.methodToProperty(LambdaUtils.extract(sFunction).getImplMethodName());
    }
}
