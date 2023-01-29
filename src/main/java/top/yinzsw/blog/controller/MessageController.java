package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.MessageVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.MessageService;

import javax.validation.Valid;

/**
 * 留言模块
 *
 * @author yinzsW
 * @since 23/01/28
 */
@Tag(name = "留言模块")
@Validated
@RestController
@RequestMapping("message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "查看留言列表")
    @GetMapping
    public PageVO<MessageVO> pageMessages(@Valid PageReq pageReq) {
        return messageService.pageMessages(pageReq);
    }
}
