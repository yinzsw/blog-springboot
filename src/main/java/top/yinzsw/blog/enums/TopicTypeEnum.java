package top.yinzsw.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主题类型枚举
 *
 * @author yinzsW
 * @since 23/02/04
 */

@Getter
@AllArgsConstructor
public enum TopicTypeEnum {

    /**
     * 文章
     */
    ARTICLE(1, "article"),

    /**
     * 说说
     */
    TALK(2, "talk"),

    /**
     * 相册
     */
    ALBUM(3, "album"),

    /**
     * 评论
     */
    COMMENT(4, "comment");

    /**
     * 主题类型名值
     */
    @EnumValue
    private final int value;

    /**
     * 主题类型名
     */
    private final String topicName;
}
