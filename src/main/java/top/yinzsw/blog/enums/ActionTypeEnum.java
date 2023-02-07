package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分数枚举
 *
 * @author yinzsW
 * @since 23/02/02
 */
@Getter
@AllArgsConstructor
public enum ActionTypeEnum {

    /**
     * 独立访问
     */
    VIEW(0.8, "view"),

    /**
     * 评论
     */
    COMMENT(1.8, "comment"),

    /**
     * 点赞
     */
    LIKE(2, "like"),

    /**
     * 倒赞
     */
    HATE(-2, "hate"),

    /**
     * 分享
     */
    SHARE(2.5, "share"),

    /**
     * 收藏
     */
    START(3, "start");

    /**
     * 行为单位价值分数
     */
    private final double value;

    /**
     * 热度名称
     */
    private final String actionName;
}
