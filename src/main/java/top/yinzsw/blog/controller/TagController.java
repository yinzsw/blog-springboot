package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagSearchVO;
import top.yinzsw.blog.model.vo.TagVO;
import top.yinzsw.blog.service.TagService;

import javax.validation.Valid;

/**
 * 标签控制器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Tag(name = "标签模块")
@Validated
@RestController
@RequestMapping("tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @Operation(summary = "查询标签列表")
    @GetMapping
    public PageVO<TagVO> pageTags(@Valid PageReq pageReq) {
        return tagService.pageTags(pageReq);
    }

    @Operation(summary = "搜索文章标签")
    @GetMapping("keywords/{keywords}")
    public PageVO<TagSearchVO> pageSearchTags(@Valid PageReq pageReq,
                                              @Parameter(description = "标签名关键词", required = true)
                                              @PathVariable("keywords") String keywords) {
        return tagService.pageSearchTags(pageReq, keywords);
    }
}
