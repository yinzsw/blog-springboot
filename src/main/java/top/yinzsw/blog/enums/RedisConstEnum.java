package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;

import java.text.MessageFormat;

/**
 * redis常量枚举类
 *
 * @author yinzsW
 * @since 23/02/02
 */
@AllArgsConstructor
public enum RedisConstEnum {

    /**
     * 记录网站当天访问用户, 统计UV
     */
    SITE_VIEWED_USERS("blog:viewed:site:users"),

    /**
     * 记录网站当天访问数量, 统计PV
     */
    SITE_VIEWED_COUNT("blog:viewed:site:count"),

    /**
     * 用户邮箱验证码<br/>
     * 0: 用户邮箱<br/>
     * value:　验证码
     */
    EMAIL_CODE("blog:email:code:{0}"),

    /**
     * 记录用户行为键 <br/>
     * 0: 主题名 请参阅: {@link TopicTypeEnum} <br/>
     * 1: 行为名 请参阅: {@link ActionTypeEnum} <br/>
     * 2: 用户标识 <br/>
     * values:　主题标志列表 <br/>
     */
    USER_ACTION_TOPICS("blog:user:{0}:{1}:{2}"),

    /**
     * 记录主题行为键 <br/>
     * 0: 主题名 请参阅: {@link TopicTypeEnum} <br/>
     * 1: 行为名 请参阅: {@link ActionTypeEnum} <br/>
     * 2: 主题标志 <br/>
     * values:　用户标识列表 <br/>
     */
    TOPIC_ACTION_USERS("blog:topic:{0}:{1}:{2}"),

    /**
     * 热度排行榜的键 <br/>
     * 0: 主题名 请参阅: {@link TopicTypeEnum} <br/>
     * values:　主题行为价值分 请参阅: {@link ActionTypeEnum}<br/>
     */
    HEAT_TOPIC_RANKING("blog:heat:{0}");

    private final String key;

    public String getKey(Object... params) {
        return MessageFormat.format(key, params);
    }
}
