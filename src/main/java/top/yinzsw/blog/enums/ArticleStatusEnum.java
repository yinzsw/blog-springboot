package top.yinzsw.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章状态枚举
 *
 * @author yinzsW
 * @since 23/01/12
 */

@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {
    /**
     * 公开
     */
    PUBLIC(1, "公开"),

    /**
     * 私密
     */
    SECRET(2, "私密"),

    /**
     * 翻译
     */
    DRAFT(3, "翻译");

    /**
     * 状态值
     */
    @EnumValue
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;
}
