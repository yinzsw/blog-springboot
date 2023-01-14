package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.request.ArticleRequest;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;
import top.yinzsw.blog.model.vo.ArticleBackVO;
import top.yinzsw.blog.model.vo.ArticleHomeVO;
import top.yinzsw.blog.model.vo.PageVO;

import javax.validation.Valid;

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
    PageVO<ArticleArchiveVO> pageListArchivesArticles(PageReq pageReq);

    /**
     * 分页查询首页文章
     *
     * @param pageReq 分页信息
     * @return 首页文章列表
     */
    PageVO<ArticleHomeVO> pageListHomeArticles(PageReq pageReq);

    /**
     * 分页查询后台文章
     *
     * @param pageReq        分页信息
     * @param articleRequest 文章查询信息
     * @return 后台文章列表
     */
    PageVO<ArticleBackVO> pageListBackArticles(@Valid PageReq pageReq, ArticleRequest articleRequest);
}
