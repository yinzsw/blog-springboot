package top.yinzsw.blog.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public static boolean isEmail(String email) {
        return EMAIL_REGEXP.matcher(email).matches();
    }

    /**
     * 判断字符串是不是全字母
     *
     * @param alpha 字符串
     * @return 是否为全字母
     */
    public static boolean isAlpha(String alpha) {
        return ALPHA_REGEXP.matcher(alpha).matches();
    }

    /**
     * 根据List<T>和响应规则获取一个映射表
     *
     * @param list        原始列表
     * @param keyFn       Map 键规则
     * @param valueItemFn Map 值列表项规则
     * @param <T>         列表泛型
     * @param <K>         Map 键类型
     * @param <D>         Map 值列表项类型
     * @return mapping 映射表
     */
    @Deprecated
    public static <T, K, D> Map<K, List<D>> getMapping(List<T> list,
                                                       Function<? super T, ? extends K> keyFn,
                                                       Function<? super T, ? extends D> valueItemFn) {
        return list.stream().collect(Collectors.groupingBy(keyFn, Collectors.mapping(valueItemFn, Collectors.toList())));
    }

}
