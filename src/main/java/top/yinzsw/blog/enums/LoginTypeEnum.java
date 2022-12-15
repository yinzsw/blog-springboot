package top.yinzsw.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式枚举
 *
 * @author yinzsW
 * @since 22/12/15
 */

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 默认登录
     */
    DEFAULT(1, "邮箱登录", ""),
    /**
     * QQ登录
     */
    QQ(2, "QQ登录", "qqLoginStrategyImpl"),
    /**
     * 微博登录
     */
    WEIBO(3, "微博登录", "weiboLoginStrategyImpl");

    /**
     * 登录方式
     */
    @EnumValue
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 策略
     */
    private final String strategy;
}
