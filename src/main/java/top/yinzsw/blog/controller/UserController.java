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
import top.yinzsw.blog.model.request.PasswordByEmailReq;
import top.yinzsw.blog.model.request.PasswordByOldReq;
import top.yinzsw.blog.model.request.UserInfoReq;
import top.yinzsw.blog.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
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

    @Operation(summary = "更新用户头像")
    @PatchMapping(value = "avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateUserAvatar(@Parameter(description = "用户头像", required = true)
                                   @MatchFileType(mimeType = "image/*", message = "仅支持上传图片类型{mimeType}的文件")
                                   @RequestPart("avatar") MultipartFile avatar) {
        return userService.updateUserAvatar(avatar);
    }

    @Operation(summary = "更新用户邮箱")
    @PatchMapping("email/{email}/{code:[^\\W_]{6}}")
    public boolean updateUserEmail(@Parameter(description = "邮箱", required = true)
                                   @Email(message = "邮箱格式不正确")
                                   @PathVariable("email") String email,
                                   @Parameter(description = "验证码", required = true)
                                   @PathVariable("code") String code) {
        return userService.updateUserEmail(email, code);
    }

    @Operation(summary = "更新用户禁用状态")
    @PatchMapping("{userId:\\d+}/isDisabled/{isDisabled:true|false}")
    public boolean updateUserIsDisable(@Parameter(description = "用户id", required = true)
                                       @PathVariable("userId") Long userId,
                                       @Parameter(description = "禁用状态", required = true)
                                       @PathVariable("isDisabled") Boolean isDisabled) {
        return userService.updateUserIsDisable(userId, isDisabled);
    }

    @Operation(summary = "更新用户密码(邮箱验证码)")
    @PatchMapping("password/email")
    public boolean updateUserPasswordByEmailCode(@Valid @RequestBody PasswordByEmailReq password) {
        return userService.updateUserPassword(password);
    }

    @Operation(summary = "更新用户密码(旧密码)")
    @PatchMapping("password/old")
    public boolean updateUserPasswordByOldPassword(@Valid @RequestBody PasswordByOldReq password) {
        return userService.updateUserPassword(password);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("info")
    public boolean updateUserInfo(@Valid @RequestBody UserInfoReq userInfoReq) {
        return userService.updateUserInfo(userInfoReq);
    }

    @Operation(summary = "修改用户角色")
    @PutMapping("{userId:\\d+}/role/{roleIds:\\d+(?:,\\d+)*}")
    public boolean updateUserRoles(@Parameter(description = "用户id", required = true)
                                   @PathVariable("userId") Long userId,
                                   @Parameter(description = "用户角色id列表", required = true)
                                   @PathVariable("roleIds") List<Long> roleIds) {
        return userService.updateUserRoles(userId, roleIds);
    }
    //TODO
    // 1. 查看在线用户
    // 2. 踢出登录(下线在线用户)
    // 3. 用户分布区域信息
    // 4. 用户注册
    // 5. 分页查询用户列表
}
