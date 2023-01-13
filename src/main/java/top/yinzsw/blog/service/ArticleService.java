package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;
import top.yinzsw.blog.model.vo.ArticleHomeVO;
import top.yinzsw.blog.model.vo.PageVO;

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
    PageVO<ArticleArchiveVO> pageListArchives(PageReq pageReq);

    /**
     * 分页查询首页文章
     *
     * @param pageReq 分页信息
     * @return 首页文章列表
     */
    PageVO<ArticleHomeVO> pageListHomeArticles(PageReq pageReq);
}
