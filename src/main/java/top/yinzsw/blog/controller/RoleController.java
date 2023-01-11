package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.model.vo.UserRoleVO;
import top.yinzsw.blog.service.RoleService;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户角色控制器
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Tag(name = "用户角色控制器")
@Validated
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "获取用户角色列表")
    @GetMapping
    public List<UserRoleVO> listUserRoles() {
        return roleService.listUserRoles();
    }

    @Operation(summary = "获取用户角色列表(分页)")
    @GetMapping("page")
    public PageVO<RoleVO> pageListUserRoles(@Valid PageReq pageReq,
                                            @Parameter(description = "用户名关键词")
                                            @RequestParam("keywords") String keywords) {
        return roleService.pageListRoles(pageReq, keywords);
    }
}
