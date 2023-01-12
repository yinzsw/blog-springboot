package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 角色,菜单关联表 通用处理层
 *
 * @author yinzsW
 * @since 23/01/02
 */

public interface RoleMtmMenuManager extends IService<RoleMtmMenuPO> {

    /**
     * 根据角色id列表 获取映射表
     *
     * @param roleIds 角色id列表
     * @return 映射表 [roleId=menuIds]
     */
    CompletableFuture<Map<Long, List<Long>>> asyncGetMappingByRoleIds(List<Long> roleIds);
}
