package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenTypeEnum {
    ACCESS(3 * 24 * 3600 * 1000L),
    REFRESH(7 * 24 * 3600 * 1000L);

    private final Long ttl;
}