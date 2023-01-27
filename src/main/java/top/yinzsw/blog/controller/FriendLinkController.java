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
import top.yinzsw.blog.model.vo.FriendLinkVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.FriendLinkService;

import javax.validation.Valid;

/**
 * 友链模块
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Tag(name = "友链模块")
@Validated
@RestController
@RequestMapping("link")
@RequiredArgsConstructor
public class FriendLinkController {
    private final FriendLinkService friendLinkService;

    @Operation(summary = "查看友链列表")
    @GetMapping("keywords/{keywords}")
    public PageVO<FriendLinkVO> pageSearchFriendLinks(@Valid PageReq pageReq,
                                                      @Parameter(description = "友链关键词", required = true)
                                                      @PathVariable("keywords") String keywords) {
        return friendLinkService.pageSearchFriendLinks(pageReq, keywords);
    }
}
