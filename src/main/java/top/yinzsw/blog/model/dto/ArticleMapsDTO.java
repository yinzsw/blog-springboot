package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.model.po.TagPO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 文章映射信息模型
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ArticleMapsDTO {
    /**
     * 分类id->分类名
     */
    private Map<Long, String> categoryNameMap = Collections.emptyMap();

    /**
     * 文章id->标签列表
     */
    private Map<Long, List<TagPO>> tagsMap = Collections.emptyMap();

    /**
     * 文章id->点赞量
     */
    private Map<Long, Long> likeCountMap = Collections.emptyMap();

    /**
     * 文章id->浏览量
     */
    private Map<Long, Long> viewCountMap = Collections.emptyMap();
}
