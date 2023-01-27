package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryDetailVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.CategoryService;

import javax.validation.Valid;

/**
 * 文章分类模块
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Tag(name = "文章分类模块")
@Validated
@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "查询分类列表")
    @GetMapping
    public PageVO<CategoryDetailVO> pageCategories(@Valid PageReq pageReq) {
        return categoryService.pageCategories(pageReq);
    }

    @Operation(summary = "搜索文章分类")
    @GetMapping("keywords/{keywords}")
    public PageVO<CategoryVO> pageSearchCategories(@Valid PageReq pageReq,
                                                   @Parameter(description = "标签名关键词", required = true)
                                                   @PathVariable("keywords") String keywords) {
        return categoryService.pageSearchCategories(pageReq, keywords);
    }

    @Operation(summary = "查询分类列表(后台)")
    @GetMapping("background/keywords/{keywords}")
    public PageVO<CategoryDetailVO> pageBackgroundSearchCategories(@Valid PageReq pageReq,
                                                                   @Parameter(description = "标签名关键词", required = true)
                                                                   @PathVariable("keywords") String keywords) {
        return categoryService.pageBackgroundSearchCategories(pageReq, keywords);
    }

    @Operation(summary = "添加或修改分类")
    @PutMapping
    public boolean saveOrUpdateCategory(@Valid @RequestBody CategoryReq categoryReq) {
        return categoryService.saveOrUpdateCategory(categoryReq);
    }
}
