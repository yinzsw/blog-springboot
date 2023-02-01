package top.yinzsw.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章类型枚举
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Getter
@AllArgsConstructor
public enum ArticleTypeEnum {

    /**
     * 原创
     */
    ORIGINAL(1, "原创"),

    /**
     * 转载
     */
    REPRINTED(2, "转载"),

    /**
     * 翻译
     */
    TRANSLATION(3, "翻译");

    /**
     * 类型值
     */
    @EnumValue
    private final int value;

    /**
     * 描述
     */
    private final String desc;
}
