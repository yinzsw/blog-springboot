package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.dto.CategoryArticleNumDTO;
import top.yinzsw.blog.model.dto.QueryBackgArticleDTO;
import top.yinzsw.blog.model.po.ArticlePO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Mapper
 * @createDate 2023-01-12 23:17:07
 * @Entity top.yinzsw.blog.model.po.ArticlePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface ArticleMapper extends CommonMapper<ArticlePO> {

    /**
     * 查找上一篇或下一篇文章
     *
     * @param curArticleId 当前文章id
     * @param userId       用户id
     * @param isPrev       上一篇/下一篇
     * @return 文章
     */
    ArticlePO getPrevOrNextArticle(@Param("curArticleId") Long curArticleId,
                                   @Param("userId") Long userId,
                                   @Param("isPrev") boolean isPrev);

    /**
     * 查询相关文章
     *
     * @param relatedArticleIds 相关文章id列表
     * @param userId            用户id
     * @return 相关文章列表
     */
    List<ArticlePO> listRelatedArticleByIds(@Param("relatedArticleIds") List<Long> relatedArticleIds,
                                            @Param("userId") Long userId);

    /**
     * 分页查询文章
     *
     * @param pager     分页器
     * @param userId    用户id
     * @param isOnlyTop 是否仅查询置顶文章
     * @return 分页文章数据
     */
    Page<ArticlePO> pageArticles(Page<ArticlePO> pager,
                                 @Param("userId") Long userId,
                                 @Param("isOnlyTop") Boolean isOnlyTop);

    /**
     * 分页查询后台文章
     *
     * @param pager 分页器
     * @param query 查询模型
     * @return 分页文章数据
     */
    Page<ArticlePO> pageBackgroundArticles(Page<Object> pager, @Param("query") QueryBackgArticleDTO query);

    /**
     * 根据关键词查找公共文章
     *
     * @param pager    分页器
     * @param keywords 关键词
     * @param userId   用户id
     * @return 公共文章
     */
    Page<ArticlePO> pagePublicArticleByKeywords(Page<ArticlePO> pager,
                                                @Param("keywords") String keywords,
                                                @Param("userId") Long userId);

    /**
     * 查询分类下的文章数量
     *
     * @param categoryIds 分类id列表
     * @param userId      用户id
     * @return 分类文章数量模型
     */
    List<CategoryArticleNumDTO> listCategoryArticleCount(@Param("categoryIds") List<Long> categoryIds,
                                                         @Param("userId") Long userId);
}




