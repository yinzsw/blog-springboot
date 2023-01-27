package top.yinzsw.blog.extension.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

/**
 * Mybatis Plus SQL 工具类
 *
 * @author yinzsW
 * @since 23/01/27
 */

public class SqlUtils {

    /**
     * 根据setter function 获取对象属性名
     *
     * @param sFunction getter
     * @param <T>       JavaBean类型
     * @param <R>       返回数据类型
     * @return 属性名
     */
    public static <T, R> String getPropertyName(SFunction<T, R> sFunction) {
        return PropertyNamer.methodToProperty(LambdaUtils.extract(sFunction).getImplMethodName());
    }
}
