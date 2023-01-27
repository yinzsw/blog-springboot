package top.yinzsw.blog.manager;

import java.util.List;

/**
 * 标签通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/27
 */

public interface TagManager {

    /**
     * 判断是否有文章使用此标签
     *
     * @param tagIds 标签id列表
     * @return 是否被使用
     */
    boolean hasUseArticle(List<Long> tagIds);
}
