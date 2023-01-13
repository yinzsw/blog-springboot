package top.yinzsw.blog.exception;

import org.springframework.dao.DataAccessException;

/**
 * 数据层异常
 *
 * @author yinzsW
 * @since 23/01/13
 */

public class DaoException extends DataAccessException {
    public DaoException(String msg) {
        super(msg);
    }
}
