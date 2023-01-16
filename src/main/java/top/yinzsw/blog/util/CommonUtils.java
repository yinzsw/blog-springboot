package top.yinzsw.blog.util;

import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.exception.DaoException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 公共工具类
 *
 * @author yinzsW
 * @since 23/01/02
 */

public class CommonUtils {
    private static final Pattern EMAIL_REGEXP = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    private static final Pattern ALPHA_REGEXP = Pattern.compile("^[a-zA-Z]+$");

    /**
     * 判断字符穿是否是邮箱
     *
     * @param email 字符串
     * @return 是否是邮箱
     */
    public static boolean getIsEmail(String email) {
        return EMAIL_REGEXP.matcher(email).matches();
    }

    /**
     * 判断字符串是不是全字母
     *
     * @param alpha 字符串
     * @return 是否为全字母
     */
    public static boolean getIsAlpha(String alpha) {
        return ALPHA_REGEXP.matcher(alpha).matches();
    }

    /**
     * 把两个列表合并成Map
     *
     * @param keys   键列表(不允许存在null值)
     * @param values 值列表(不允许存在null值)
     * @param <K>    键
     * @param <V>    值
     * @return 合并后的Map
     */
    public static <K, V> Map<K, V> mergeList(List<K> keys, List<V> values) {
        if (CollectionUtils.isEmpty(keys) || CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        return IntStream.range(0, Math.min(keys.size(), values.size())).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    /**
     * 将两个异步任务的结果根据自定义回调方法进行合并
     *
     * @param future1 异步任务1
     * @param future2 异步任务2
     * @param handle  回调方法
     * @param <T>     future1
     * @param <K>     future2
     * @param <R>     result
     * @return 合并结果
     */
    public static <T, K, R> R biCompletableFuture(CompletableFuture<T> future1,
                                                  CompletableFuture<K> future2,
                                                  BiFunction<T, K, R> handle) {
        return CompletableFuture.allOf(future1, future2)
                .thenApply(unused -> handle.apply(future1.join(), future2.join()))
                .exceptionally(e -> {
                    throw new DaoException(e.getMessage());
                }).join();
    }
}
