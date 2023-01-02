package top.yinzsw.blog.constant;

/**
 * 消息队列常量
 *
 * @author yinzsW
 * @since 22/12/31
 */

public final class MQConst {

    /**
     * 邮件交换机
     */
    public static final String EMAIL_EXCHANGE = "blog.email.exchange";

    /**
     * 发送验证码邮件的队列
     */
    public static final String EMAIL_CODE_QUEUE = "email.code.queue";

    /**
     * 发送验证码邮件的路由键
     */
    public static final String EMAIL_CODE_KEY = "email.code.key";
}
