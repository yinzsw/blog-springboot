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
import top.yinzsw.blog.model.request.UserInfoRequest;
import top.yinzsw.blog.service.UserService;

import javax.validation.Valid;

/**
 * 用户控制层
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Tag(name = "用户模块")
@Validated
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "更新用户信息")
    @PutMapping("info")
    public Boolean updateUserInfo(@Valid @RequestBody UserInfoRequest userInfoRequest) {
        return userService.updateUserInfo(userInfoRequest);
    }

    @Operation(summary = "更新用户头像")
    @PatchMapping(value = "avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String updateUserAvatar(@Parameter(description = "文件", required = true)
                                   @MatchFileType(message = "仅支持上传图片类型的文件", mimeType = "image/*")
                                   @RequestPart("file") MultipartFile file) {
        return userService.updateUserAvatar(file);
    }
}
