package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service
 * @createDate 2023-01-12 23:17:07
 */
public interface ArticleService extends IService<ArticlePO> {

    /**
     * 分页查询文章归档列表
     *
     * @param pageReq 分页信息
     * @return 文章归档列表
     */
    PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq);

    /**
     * 查看首页文章
     *
     * @param articleId 文章id
     * @return 首页文章
     */
    ArticleHomeVO getHomeArticle(Long articleId);

    /**
     * 获取首页置顶文章
     *
     * @return 置顶文章列表
     */
    List<ArticleDigestHomeVO> listHomeTopArticles();

    /**
     * 分页查询首页文章
     *
     * @param pageReq 分页信息
     * @return 首页文章列表
     */
    PageVO<ArticleDigestHomeVO> pageHomeArticles(PageReq pageReq);

    /**
     * 查看后台文章
     *
     * @param articleId 文章id
     * @return 后台文章
     */
    ArticleBackVO getBackArticle(Long articleId);

    /**
     * 分页查询后台文章
     *
     * @param pageReq         分页信息
     * @param articleQueryReq 文章查询信息
     * @return 后台文章列表
     */
    PageVO<ArticleDigestBackVO> pageBackArticles(@Valid PageReq pageReq, ArticleQueryReq articleQueryReq);

    /**
     * 修改置顶状态
     *
     * @param articleId 文章id
     * @param isTop     是否置顶
     * @return 是否成功
     */
    boolean updateArticleIsTop(Long articleId, Boolean isTop);

    /**
     * 恢复或删除文章
     *
     * @param articleId 文章id
     * @param isDeleted 是否删除
     * @return 是否成功
     */
    boolean updateArticleIsDeleted(Long articleId, Boolean isDeleted);

    /**
     * 保存文章
     *
     * @param articleReq 文章信息
     * @return 是否成功
     */
    boolean saveOrUpdateArticle(ArticleReq articleReq);

    /**
     * 批量删除文章
     *
     * @param articleIds 文章id
     * @return 是否成功
     */
    boolean deleteArticles(List<Long> articleIds);

}
