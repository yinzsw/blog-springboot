package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;
import top.yinzsw.blog.model.vo.ArticleBackVO;
import top.yinzsw.blog.model.vo.ArticleHomeVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.ArticleService;

import javax.validation.Valid;

/**
 * 文章控制器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Tag(name = "文章模块")
@Validated
@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "查看文章归档(分页)")
    @GetMapping("archives/page")
    public PageVO<ArticleArchiveVO> pageListArchives(@Valid PageReq pageReq) {
        return articleService.pageListArchivesArticles(pageReq);
    }

    @Operation(summary = "查看首页文章(分页)")
    @GetMapping("home/page")
    public PageVO<ArticleHomeVO> pageListHomeArticles(@Valid PageReq pageReq) {
        return articleService.pageListHomeArticles(pageReq);
    }

    @Operation(summary = "查看后台文章(分页)")
    @GetMapping("back/page")
    public PageVO<ArticleBackVO> pageListBackArticles(@Valid PageReq pageReq,
                                                      @Valid ArticleQueryReq articleQueryReq) {
        return articleService.pageListBackArticles(pageReq, articleQueryReq);
    }

    @Operation(summary = "添加或修改文章")
    @PutMapping
    public boolean saveOrUpdateArticle(@Valid @RequestBody ArticleReq articleReq) {
        return articleService.saveOrUpdateArticle(articleReq);
    }
}
