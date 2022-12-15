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
    AUTHENTICATED_ERROR(32000, "身份认证失败"),

    /**
     * 操作未授权
     */
    UNAUTHORIZED(33000, "操作未授权"),

    /**
     * 无效参数异常
     */
    NOT_VALID_PARAMS(40000, "无效参数异常"),

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
