package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.model.po.TagPO;

import java.util.List;
import java.util.Map;

/**
 * 文章映射信息传输对象
 *
 * @author yinzsW
 * @since 23/01/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ArticleMappingDTO {

    /**
     * 分类id->分类名
     */
    private Map<Long, String> categoryNameMapping;

    /**
     * 文章id->标签列表
     */
    private Map<Long, List<TagPO>> tagMapping;

    /**
     * 文章id->点赞量
     */
    private Map<Long, Long> likeCountMapping;

    /**
     * 文章id->浏览量
     */
    private Map<Long, Long> viewCountMapping;
}
