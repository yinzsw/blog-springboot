package top.yinzsw.blog.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

/**
 * 分页工具类
 *
 * @author yinzsW
 * @since 2022/12/15
 **/
public class PageUtils {
    public static final String DEFAULT_SIZE = "10";
    public static final String PARAMS_PAGE = "page";
    public static final String PARAMS_SIZE = "size";

    private static final ThreadLocal<Page<?>> PAGE_HOLDER = new ThreadLocal<>();

    public static void setCurrentPage(Page<?> page) {
        PAGE_HOLDER.set(page);
    }

    public static Page<?> getPage() {
        Page<?> page = PAGE_HOLDER.get();
        if (Objects.isNull(page)) {
            setCurrentPage(new Page<>());
        }
        return PAGE_HOLDER.get();
    }

    public static long getCurrent() {
        return getPage().getCurrent();
    }

    public static long getSize() {
        return getPage().getSize();
    }

    public static long getOffset() {
        return (getPage().getCurrent() - 1) * getSize();
    }

    public static void remove() {
        PAGE_HOLDER.remove();
    }
}
