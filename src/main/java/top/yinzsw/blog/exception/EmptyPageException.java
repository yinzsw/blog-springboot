package top.yinzsw.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分页模型记录为空异常
 *
 * @author yinzsW
 * @since 23/01/22
 */

@Getter
@AllArgsConstructor
public class EmptyPageException extends RuntimeException {

    private final Long totalCount;
}
