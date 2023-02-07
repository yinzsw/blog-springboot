package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.po.ArticlePO;

import java.util.List;

/**
 * 文章通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/13
 */

public interface ArticleManager extends CommonManager<ArticlePO> {

    /**
     * 修改文章浏览量
     *
     * @param articleId    文章id
     * @param userIdentify 用户唯一标志
     */
    void updateViewsInfo(Long articleId, String userIdentify);

    //////////////////////////////////////////////////////MYSQL/////////////////////////////////////////////////////////

    /**
     * 获取文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    ArticlePO getArticleById(Long articleId);

    /**
     * 分页获取文章
     *
     * @param pager      分页器
     * @param categoryId 分类id
     * @return 预览文章 分页模型
     */
    Page<ArticlePO> pagePreviewArticlesByCategoryId(Page<ArticlePO> pager, Long categoryId);

    /**
     * 分页获取文章
     *
     * @param pager      分页器
     * @param articleIds 文章id列表
     * @return 预览文章 分页模型
     */
    Page<ArticlePO> pagePreviewArticlesByIds(Page<ArticlePO> pager, List<Long> articleIds);

    /**
     * 分页获取用户的公共文章和私密文章归档
     *
     * @param pager 分页器
     * @return 分页内容
     */
    Page<ArticlePO> pageArchivesArticles(Page<ArticlePO> pager);

    ///////////////////////////////////////////////////MapsContext//////////////////////////////////////////////////////

    /**
     * 获取文章分类信息,标签信息,热度信息
     *
     * @param articlePOList 文章原始对象列表
     * @param mapHotIndex   是否映射热度信息
     * @return 映射上下文模型
     */
    ArticleMapsDTO getArticleMapsDTO(List<ArticlePO> articlePOList, boolean mapHotIndex);
}
