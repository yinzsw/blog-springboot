package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.UserInfoRequest;
import top.yinzsw.blog.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制层
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Tag(name = "用户模块")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "获取用户信息")
    @GetMapping
    public List<UserPO> getUsers() {
        return userService.list();
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("info")
    public Boolean updateUserInfo(@RequestBody @Valid UserInfoRequest userInfoRequest) {
        return userService.updateUserInfo(userInfoRequest);
    }
}
