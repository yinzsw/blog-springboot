package top.yinzsw.blog.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.exception.EmptyPageException;

import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author yinzsW
 * @since 23/01/02
 */

public class VerifyUtils {
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
     * 检查纷纷也模型是否为空
     *
     * @param page 分页模型
     * @param <T>  数据类型
     */
    public static <T> void checkIPage(IPage<T> page) {
        if (CollectionUtils.isEmpty(page.getRecords())) {
            throw new EmptyPageException(page.getTotal());
        }
    }
}
