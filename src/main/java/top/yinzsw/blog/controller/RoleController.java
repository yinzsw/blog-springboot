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
import javax.validation.constraints.NotEmpty;
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
        return roleService.listUserRoles();
    }

    @Operation(summary = "获取角色列表(分页)")
    @GetMapping("page")
    public PageVO<RoleVO> pageListRoles(@Valid PageReq pageReq,
                                        @Parameter(description = "用户名关键词")
                                        @RequestParam("keywords") String keywords) {
        return roleService.pageListRoles(pageReq, keywords);
    }

    @Operation(summary = "保存或更新角色")
    @PutMapping
    public boolean saveOrUpdateRole(@Valid @RequestBody RoleReq roleReq) {
        return roleService.saveOrUpdateRole(roleReq);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping
    public boolean deleteRoles(@Parameter(description = "角色id列表", required = true)
                               @NotEmpty(message = "角色id不能为空")
                               @RequestParam("roleIdList") List<Long> roleIdList) {
        return roleService.deleteRoles(roleIdList);
    }
}
