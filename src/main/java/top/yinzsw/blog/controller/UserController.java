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
import top.yinzsw.blog.manager.UserMtmRoleManager;
import top.yinzsw.blog.model.request.PasswordByEmailReq;
import top.yinzsw.blog.model.request.PasswordByOldReq;
import top.yinzsw.blog.model.request.UserInfoReq;
import top.yinzsw.blog.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户控制器
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
    private final UserMtmRoleManager userMtmRoleManager;

    @Operation(summary = "更新用户信息")
    @PutMapping("info")
    public Boolean updateUserInfo(@Valid @RequestBody UserInfoReq userInfoReq) {
        return userService.updateUserInfo(userInfoReq);
    }

    @Operation(summary = "更新用户头像")
    @PatchMapping(value = "avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String updateUserAvatar(@Parameter(description = "文件", required = true)
                                   @NotNull(message = "上传文件不能为空")
                                   @MatchFileType(mimeType = "image/*", message = "仅支持上传图片类型{mimeType}的文件")
                                   @RequestPart("file") MultipartFile file) {
        return userService.updateUserAvatar(file);
    }

    @Operation(summary = "更新用户邮箱")
    @PatchMapping("email/{email}")
    public Boolean updateUserEmail(@Parameter(description = "邮箱", required = true)
                                   @NotBlank(message = "邮箱不能为空")
                                   @Email(message = "邮箱格式不正确")
                                   @PathVariable("email") String email,
                                   @Parameter(description = "验证码")
                                   @NotBlank(message = "验证码不能为空")
                                   @RequestParam("code") String code) {
        return userService.updateUserEmail(email, code);
    }

    @Operation(summary = "更新用户禁用状态")
    @PatchMapping("disable/{userId:\\d+}")
    public Boolean updateUserDisable(@Parameter(description = "用户id", required = true)
                                     @PathVariable("userId") Long userId,
                                     @Parameter(description = "禁用状态")
                                     @RequestParam("disable") Boolean disable) {
        return userService.updateUserDisable(userId, disable);
    }

    @Operation(summary = "更新用户密码(邮箱验证码)")
    @PatchMapping("password/email")
    public Boolean updateUserPasswordByEmailCode(@Valid @RequestBody PasswordByEmailReq password) {
        return userService.updateUserPassword(password);
    }

    @Operation(summary = "更新用户密码(旧密码)")
    @PatchMapping("password/old")
    public Boolean updateUserPasswordByOldPassword(@Valid @RequestBody PasswordByOldReq password) {
        return userService.updateUserPassword(password);
    }

    @Operation(summary = "修改用户角色")
    @PutMapping("role/{userId:\\d+}")
    public Boolean updateUserRole(@Parameter(description = "用户id", required = true)
                                  @Min(value = 1, message = "不合法的用户id: ${validatedValue}")
                                  @PathVariable("userId") Long userId,
                                  @Parameter(description = "用户角色id列表", required = true)
                                  @RequestBody List<Long> roleIds) {
        return userMtmRoleManager.updateUserRoles(userId, roleIds);
    }
}
