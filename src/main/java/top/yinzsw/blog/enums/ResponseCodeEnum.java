package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author yinzsW
 * @since 22/12/15
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    /**
     * 成功
     */
    SUCCESS(20000, "成功"),

    /**
     * 登录失败
     */
    LOGIN_ERROR(30000, "登录失败"),

    /**
     * 身份未认证
     */
    UNAUTHENTICATED(32000, "身份未认证"),

    /**
     * 操作未授权
     */
    UNAUTHORIZED(33000, "授权未操作"),

    /**
     * token过期
     */
    TOKEN_EXPIRED(34000, "token信息过期"),

    /**
     * token校验失败
     */
    TOKEN_ERROR(35000, "token校验失败"),

    /**
     * 无效参数异常
     */
    NOT_VALID_PARAMS(40000, "无效的请求参数"),

    /**
     * 操作失败
     */
    FAIL(45000, "失败"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常");

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String desc;
}
