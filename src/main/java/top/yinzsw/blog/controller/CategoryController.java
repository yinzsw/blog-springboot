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
import java.util.List;

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

    @Operation(summary = "搜索文章分类")
    @GetMapping("name/{name}")
    public PageVO<CategoryVO> pageSearchCategories(@Valid PageReq pageReq,
                                                   @Parameter(description = "分类名关键词", required = true)
                                                   @PathVariable("name") String name) {
        return categoryService.pageSearchCategories(pageReq, name);
    }

    @Operation(summary = "查询分类列表")
    @GetMapping("detail/name/{name}")
    public PageVO<CategoryDetailVO> pageDetailCategories(@Valid PageReq pageReq,
                                                         @Parameter(description = "分类名关键词", required = true)
                                                         @PathVariable("name") String name) {
        return categoryService.pageDetailCategories(pageReq, name);
    }

    @Operation(summary = "添加或修改分类")
    @PutMapping
    public boolean saveOrUpdateCategory(@Valid @RequestBody CategoryReq categoryReq) {
        return categoryService.saveOrUpdateCategory(categoryReq);
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("{categoryIds:\\d+(?:,\\d+)*}")
    public boolean deleteCategories(@Parameter(description = "分类id列表", required = true)
                                    @PathVariable("categoryIds") List<Long> categoryIds) {
        return categoryService.deleteCategories(categoryIds);
    }
}
