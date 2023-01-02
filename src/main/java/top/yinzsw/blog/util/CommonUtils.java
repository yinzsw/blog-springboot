package top.yinzsw.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 *
 * @author yinzsW
 * @since 23/01/02
 */

public class CommonUtils {
    private static final Pattern EMAIL_REGEXP = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /**
     * 判断字符穿是否是邮箱
     *
     * @param email 字符串
     * @return 是否是邮箱
     */
    public static boolean isEmail(String email) {
        Matcher emailMatcher = EMAIL_REGEXP.matcher(email);
        return emailMatcher.matches();
    }
}
