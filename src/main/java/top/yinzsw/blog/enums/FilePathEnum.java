package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件保存路径枚举
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 头像路径
     */
    AVATAR("avatar", "头像路径");

    private final String path;
    private final String desc;
}
