package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.extension.validation.MatchFileType;
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
    private final UploadProvider uploadProvider;

    @Operation(summary = "查看文章归档(分页)")
    @GetMapping("archives/page")
    public PageVO<ArticleArchiveVO> pageArchivesArticles(@Valid PageReq pageReq) {
        return articleService.pageArchivesArticles(pageReq);
    }

    @Operation(summary = "查看首页文章(分页)")
    @GetMapping("home/page")
    public PageVO<ArticleHomeVO> pageHomeArticles(@Valid PageReq pageReq) {
        return articleService.pageHomeArticles(pageReq);
    }

    @Operation(summary = "查看后台文章(分页)")
    @GetMapping("back/page")
    public PageVO<ArticleBackVO> pageBackArticles(@Valid PageReq pageReq,
                                                  @Valid ArticleQueryReq articleQueryReq) {
        return articleService.pageBackArticles(pageReq, articleQueryReq);
    }

    @Operation(summary = "添加或修改文章")
    @PutMapping
    public boolean saveOrUpdateArticle(@Valid @RequestBody ArticleReq articleReq) {
        return articleService.saveOrUpdateArticle(articleReq);
    }

    @Operation(summary = "修改文章置顶状态")
    @PatchMapping("{articleId:\\d+}/isTop/{isTop:true|false}")
    public boolean updateArticleIsTop(@Parameter(description = "文章id", required = true)
                                      @PathVariable("articleId") Long articleId,
                                      @Parameter(description = "是否置顶", required = true)
                                      @PathVariable("isTop") Boolean isTop) {
        return articleService.updateArticleIsTop(articleId, isTop);
    }

    @Operation(summary = "恢复或删除文章")
    @PatchMapping("{articleId:\\d+}/isDeleted/{isDeleted:true|false}")
    public boolean updateArticleIsDeleted(@Parameter(description = "文章id", required = true)
                                          @PathVariable("articleId") Long articleId,
                                          @Parameter(description = "是否删除", required = true)
                                          @PathVariable("isDeleted") Boolean isDeleted) {
        return articleService.updateArticleIsDeleted(articleId, isDeleted);
    }

    @Operation(summary = "上传文章图片")
    @PatchMapping(value = "image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveArticleImages(@Parameter(description = "文章图片", required = true)
                                    @MatchFileType(mimeType = "image/*", message = "仅支持上传图片类型{mimeType}的文件")
                                    @RequestPart("image") MultipartFile image) {
        return uploadProvider.uploadFile(FilePathEnum.ARTICLE.getPath(), image);
    }
}
