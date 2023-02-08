package top.yinzsw.blog.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * token类型枚举
 *
 * @author yinzsW
 * @since 23/01/15
 **/
@Getter
@AllArgsConstructor
public enum TokenTypeEnum {
    ACCESS(0, 24 * 3600),
    REFRESH(1, 3 * 24 * 3600);

    /**
     * 类型
     */
    @JsonValue
    private final int type;

    /**
     * 存活时间 秒
     */
    private final long ttl;
}