package top.yinzsw.blog.service;

import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service
 * @createDate 2023-01-12 23:17:07
 */
public interface ArticleService {

    /**
     * 查看文章详情
     *
     * @param articleId 文章id
     * @return 文章详情
     */
    ArticleVO getArticle(Long articleId);

    /**
     * 查看后台文章详情
     *
     * @param articleId 文章id
     * @return 后台文章详情
     */
    ArticleBackgroundVO getBackgroundArticle(Long articleId);

    /**
     * 查询相关文章
     *
     * @param articleId 文章id
     * @return 相关文章列表
     */
    List<ArticleSummaryVO> listRelatedArticles(Long articleId);

    /**
     * 分页查询文章
     *
     * @param pageReq   分页信息
     * @param isOnlyTop 仅仅查询置顶文章
     * @return 文章列表
     */
    PageVO<ArticleDigestVO> pageArticles(PageReq pageReq, Boolean isOnlyTop);

    /**
     * 分页查询后台文章
     *
     * @param pageReq         分页信息
     * @param articleQueryReq 文章查询信息
     * @return 后台文章列表
     */
    PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(PageReq pageReq, ArticleQueryReq articleQueryReq);

    /**
     * 根据分类id查看文章预览
     *
     * @param pageReq    分页信息
     * @param categoryId 分类id
     * @return 文章预览信息
     */
    PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, Long categoryId);

    /**
     * 根据标签id查看文章预览
     *
     * @param pageReq 分页信息
     * @param tagIds  标签id列表
     * @return 文章预览信息
     */
    PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, List<Long> tagIds);

    /**
     * 分页查询文章归档列表
     *
     * @param pageReq 分页信息
     * @return 文章归档列表
     */
    PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq);

    /**
     * 根据关键词搜索文章
     *
     * @param pageReq  分页信息
     * @param keywords 关键词
     * @return 搜索文章列表
     */
    PageVO<ArticleSearchVO> pageSearchArticles(PageReq pageReq, String keywords);

    /**
     * 上传文章图片
     *
     * @param image 图片
     * @return 图片地址
     */
    String uploadFileArticleImage(MultipartFile image);

    /**
     * 文章点赞或取消点赞
     *
     * @param articleId 文章id
     * @param like      点赞状态
     * @return 是否成功
     */
    boolean updateArticleIsLiked(Long articleId, Boolean like);

    /**
     * 修改置顶状态
     *
     * @param articleId 文章id
     * @param isTop     是否置顶
     * @return 是否成功
     */
    boolean updateArticleIsTop(Long articleId, Boolean isTop);

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
