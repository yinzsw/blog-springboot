package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.LoginRequest;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;
import top.yinzsw.blog.service.AuthService;

import javax.validation.Valid;

/**
 * 用户认证控制器
 *
 * @author yinzsW
 * @since 22/12/19
 */
@Tag(name = "用户认证控制器")
@Validated
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("login")
    public UserInfoVO login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Operation(summary = "刷新凭据")
    @GetMapping("refresh")
    public TokenVO refresh() {
        return authService.refreshToken();
    }

    @Operation(summary = "用户注销")
    @DeleteMapping("logout")
    public Boolean logout() {
        return authService.logout();
    }
}
