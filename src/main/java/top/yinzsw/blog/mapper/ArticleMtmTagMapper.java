package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.dto.TagArticleNumDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【article_mtm_tag(文章与标签映射表(多对多))】的数据库操作Mapper
 * @createDate 2023-01-13 09:59:04
 * @Entity top.yinzsw.blog.model.po.ArticleMtmTagPO
 */

@CacheNamespace(readWrite = false, blocking = true)
public interface ArticleMtmTagMapper extends CommonMapper<ArticleMtmTagPO> {

    /**
     * 通过标签id获取相关文章id
     *
     * @param excludeArticleId 排除的文章id
     * @param tagIds           标签id列表
     * @return 文章id列表
     */
    List<Long> listRelatedArticleIdsByTagIds(@Param("excludeArticleId") Long excludeArticleId, @Param("tagIds") List<Long> tagIds);

    /**
     * 查询标签下的文章数量
     *
     * @param tagIds 标签id列表
     * @return 标签文章数量模型
     */
    List<TagArticleNumDTO> listTagArticleCount(@Param("tagIds") List<Long> tagIds);
}