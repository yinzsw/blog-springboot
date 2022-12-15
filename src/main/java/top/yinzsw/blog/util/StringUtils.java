package top.yinzsw.blog.util;

/**
 * String 工具类
 *
 * @author yinzsW
 * @since 22/12/15
 */

public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isBlank();
    }

    public static boolean nonNullAndEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean nonNullAndBlank(String str) {
        return !isNullOrBlank(str);
    }
}
