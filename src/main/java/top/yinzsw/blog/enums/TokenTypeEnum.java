package top.yinzsw.blog.enums;

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
    ACCESS(3 * 24 * 3600 * 1000L),
    REFRESH(7 * 24 * 3600 * 1000L);

    private final long ttl;
}