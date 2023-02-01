package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.MessageContentReq;
import top.yinzsw.blog.model.request.MessageQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.MessageBackgroundVO;
import top.yinzsw.blog.model.vo.MessageVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.MessageService;

import javax.validation.Valid;
import java.util.List;

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

    @Operation(summary = "查看留言列表(后台)")
    @GetMapping("background")
    public PageVO<MessageBackgroundVO> pageBackgroundMessages(@Valid PageReq pageReq,
                                                              @Valid MessageQueryReq messageQueryReq) {
        return messageService.pageBackgroundMessages(pageReq, messageQueryReq);
    }

    @Operation(summary = "添加留言")
    @PostMapping
    public boolean saveMessage(@Valid @RequestBody MessageContentReq messageReq) {
        return messageService.saveMessage(messageReq);
    }

    @Operation(summary = "批量审核留言")
    @PatchMapping("{messageIds:\\d+(?:,\\d+)*}/isReview/{isReview:true|false}")
    public boolean updateMessagesIsReview(@Parameter(description = "留言id", required = true)
                                          @PathVariable("messageIds") List<Long> messageIds,
                                          @Parameter(description = "标签名关键词", required = true)
                                          @PathVariable("isReview") Boolean isReview) {
        return messageService.updateMessagesIsReview(messageIds, isReview);
    }

    @Operation(summary = "删除留言")
    @DeleteMapping("{messageIds:\\d+(?:,\\d+)*}")
    public boolean deleteMessages(@Parameter(description = "留言id", required = true)
                                  @PathVariable("messageIds") List<Long> messageIds) {
        return messageService.deleteMessages(messageIds);
    }
}
