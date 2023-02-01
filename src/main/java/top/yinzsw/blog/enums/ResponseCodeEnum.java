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
     * 无效的请求
     */
    INVALID_REQUEST(10000, "无效的请求"),

    /**
     * 成功
     */
    SUCCESS(20000, "成功"),

    /**
     * 身份未认证
     */
    AUTHENTICATED_FAIL(31000, "身份未认证"),

    /**
     * 身份认证过期
     */
    AUTHENTICATED_EXPIRED(32000, "身份认证过期"),

    /**
     * 操作未授权
     */
    FORBIDDEN(33000, "权限不足, 禁止访问"),

    /**
     * 无效参数异常
     */
    NOT_VALID_PARAMS(40000, "无效的请求参数"),

    /**
     * 缺少必要参数
     */
    MISSING_REQUIRED_PARAMS(41000, "缺少必要参数"),

    /**
     * 文件上传出错
     */
    FILE_UPLOAD_ERROR(43000, "文件上传出错"),

    /**
     * 操作失败
     */
    FAIL(45000, "失败"),

    /**
     * 数据持久层异常
     */
    DAO_ERROR(49000, "数据层异常"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 描述
     */
    private final String desc;
}
