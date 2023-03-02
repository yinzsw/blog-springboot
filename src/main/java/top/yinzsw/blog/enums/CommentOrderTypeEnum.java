package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评论排序类型
 *
 * @author yinzsW
 * @since 23/02/13
 */
@Getter
@AllArgsConstructor
public enum CommentOrderTypeEnum {
    HOT(1), DATE(2);

    private final int value;
}
