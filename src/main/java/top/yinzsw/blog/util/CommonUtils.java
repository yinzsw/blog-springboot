package top.yinzsw.blog.util;

import java.util.regex.Pattern;

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
}
