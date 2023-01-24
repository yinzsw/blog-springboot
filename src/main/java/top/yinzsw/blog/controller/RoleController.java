package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.RoleReq;
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
@Tag(name = "角色模块")
@Validated
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "获取角色列表")
    @GetMapping("list")
    public List<UserRoleVO> listRoles() {
        return roleService.listRoles();
    }

    @Operation(summary = "获取角色列表(分页)")
    @GetMapping("page")
    public PageVO<RoleVO> pageRoles(@Valid PageReq pageReq,
                                    @Parameter(description = "用户名关键词")
                                    @RequestParam("keywords") String keywords) {
        return roleService.pageRoles(pageReq, keywords);
    }

    @Operation(summary = "更新角色禁用状态")
    @PatchMapping("{roleId:\\d+}/{isDisabled:true|false}")
    public boolean updateRoleIsDisabled(@Parameter(description = "角色id", required = true)
                                        @PathVariable("roleId") Long roleId,
                                        @Parameter(description = "禁用状态", required = true)
                                        @PathVariable("isDisabled") Boolean isDisabled) {
        return roleService.updateRoleIsDisabled(roleId, isDisabled);
    }

    @Operation(summary = "保存或更新角色")
    @PutMapping
    public boolean saveOrUpdateRole(@Valid @RequestBody RoleReq roleReq) {
        return roleService.saveOrUpdateRole(roleReq);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("{roleIds:\\d+(?:,\\d+)*}")
    public boolean deleteRoles(@Parameter(description = "角色id列表", required = true)
                               @PathVariable("roleIds") List<Long> roleIds) {
        return roleService.deleteRoles(roleIds);
    }
}
