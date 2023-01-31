package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.extension.validation.MatchFileType;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.ArticleService;

import javax.validation.Valid;
import java.util.List;

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

    @Operation(summary = "搜索相关文章")
    @GetMapping("keywords/{keywords}")
    public List<ArticleSearchVO> listSearchArticles(@Parameter(description = "搜索关键字", required = true)
                                                    @PathVariable("keywords") String keywords) {
        return articleService.listSearchArticles(keywords);
    }

    @Operation(summary = "查看文章归档")
    @GetMapping("archives")
    public PageVO<ArticleArchiveVO> pageArchivesArticles(@Valid PageReq pageReq) {
        return articleService.pageArchivesArticles(pageReq);
    }

    @Operation(summary = "查看文章详情")
    @GetMapping("{articleId:\\d+}")
    public ArticleVO getArticle(@Parameter(description = "文章id", required = true)
                                @PathVariable("articleId") Long articleId) {
        return articleService.getArticle(articleId);
    }

    @Operation(summary = "查看文章列表")
    @GetMapping("isTop/{isTop:true|false}")
    public PageVO<ArticleDigestVO> pageArticles(@Valid PageReq pageReq,
                                                @Parameter(description = "是否置顶", required = true)
                                                @PathVariable("isTop") Boolean isTop) {
        return articleService.pageArticles(pageReq, isTop);
    }

    @Operation(summary = "查看文章预览(分类ID)")
    @GetMapping("category/{categoryId:\\d+}")
    public PageVO<ArticlePreviewVO> pagePreviewArticles(@Valid PageReq pageReq,
                                                        @Parameter(description = "分类id", required = true)
                                                        @PathVariable("categoryId") Long categoryId) {
        return articleService.pagePreviewArticles(pageReq, categoryId);
    }

    @Operation(summary = "查看文章预览(标签ID)")
    @GetMapping("tag/{tagIds:\\d+(?:,\\d+)*}")
    public PageVO<ArticlePreviewVO> pagePreviewArticles(@Valid PageReq pageReq,
                                                        @Parameter(description = "标签id", required = true)
                                                        @PathVariable("tagIds") List<Long> tagIds) {
        return articleService.pagePreviewArticles(pageReq, tagIds);
    }

    @Operation(summary = "查看文章详情(后台)")
    @GetMapping("background/{articleId:\\d+}")
    public ArticleBackgroundVO getBackgroundArticle(@Parameter(description = "文章id", required = true)
                                                    @PathVariable("articleId") Long articleId) {
        return articleService.getBackgroundArticle(articleId);
    }

    @Operation(summary = "查看文章列表(后台)")
    @GetMapping("background")
    public PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(@Valid PageReq pageReq,
                                                                    @Valid ArticleQueryReq articleQueryReq) {
        return articleService.pageBackgroundArticles(pageReq, articleQueryReq);
    }

    @Operation(summary = "上传文章图片")
    @PostMapping(value = "image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFileArticleImage(@Parameter(description = "文章图片", required = true)
                                         @MatchFileType(mimeType = "image/*", message = "仅支持上传图片类型{mimeType}的文件")
                                         @RequestPart("image") MultipartFile image) {
        return articleService.uploadFileArticleImage(image);
    }

    @Operation(summary = "点赞文章")
    @PatchMapping("{articleId:\\d+}/like/{like:true|false}")
    public boolean likeArticle(@Parameter(description = "文章id", required = true)
                               @PathVariable("articleId") Long articleId,
                               @Parameter(description = "点赞状态", required = true)
                               @PathVariable("like") Boolean like) {
        return articleService.likeArticle(articleId, like);
    }

    @Operation(summary = "修改文章置顶状态")
    @PatchMapping("{articleId:\\d+}/isTop/{isTop:true|false}")
    public boolean updateArticleIsTop(@Parameter(description = "文章id", required = true)
                                      @PathVariable("articleId") Long articleId,
                                      @Parameter(description = "是否置顶", required = true)
                                      @PathVariable("isTop") Boolean isTop) {
        return articleService.updateArticleIsTop(articleId, isTop);
    }

    @Operation(summary = "添加或修改文章")
    @PutMapping
    public boolean saveOrUpdateArticle(@Valid @RequestBody ArticleReq articleReq) {
        return articleService.saveOrUpdateArticle(articleReq);
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("{articleIds:\\d+(?:,\\d+)*}")
    public boolean deleteArticles(@Parameter(description = "文章id列表", required = true)
                                  @PathVariable("articleIds") List<Long> articleIds) {
        return articleService.deleteArticles(articleIds);
    }

    //TODO 文章导入与导出
}
