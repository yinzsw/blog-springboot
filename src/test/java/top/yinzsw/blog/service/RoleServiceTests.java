package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.manager.RoleMtmMenuManager;
import top.yinzsw.blog.manager.RoleMtmResourceManager;
import top.yinzsw.blog.manager.UserMtmRoleManager;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.List;

/**
 * Describe your code
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Slf4j
@SpringBootTest
public class RoleServiceTests {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMtmMenuManager roleMtmMenuManager;
    @Autowired
    private RoleMtmResourceManager roleMtmResourceManager;

    @Autowired
    private UserMtmRoleManager userMtmRoleManager;

    @Test
    void roleMtmMenuManagerTest() {
//
//        var mapCompletableFuture = roleMtmMenuManager.asyncGetMappingByRoleIds(List.of(1L));
//        System.out.println(mapCompletableFuture.join());

        List<Long> roleIds = List.of(1L);
        roleMtmMenuManager.getMappingByRoleIds(roleIds)
                .thenCombine(
                        roleMtmResourceManager.getMappingByRoleIds(roleIds),
                        (longListMap, longListMap2) -> {
                            System.out.println(longListMap);
                            System.out.println(longListMap2);
                            return "";
                        })
                .join();
    }

    @RepeatedTest(5)
    void getRolesByUserIdTest() {
        List<String> rolesByUserId = roleService.getRoleNamesByUserId(1L);
        roleService.listObjs(new LambdaQueryWrapper<RolePO>()
                .select(RolePO::getRoleLabel)
                .eq(RolePO::getIsDisabled, false)
                .in(RolePO::getId, 1L), Object::toString);
        log.info("rolesByUserId: {}", rolesByUserId);
    }

    @Test
    void listRoleIdsByResourceIdTest() {
        var result1 = roleMtmResourceManager.lambdaQuery()
                .select(RoleMtmResourcePO::getRoleId)
                .eq(RoleMtmResourcePO::getResourceId, 330L)
                .list();
        System.out.println(result1);


        var result2 = userMtmRoleManager.lambdaQuery()
                .select(UserMtmRolePO::getRoleId)
                .eq(UserMtmRolePO::getUserId, 996L)
                .list();
        System.out.println(result2);
    }


    @RepeatedTest(5)
    void getRoles() {
        roleService.list();
    }

    @SneakyThrows
    @Test
    void getRolesThread() {
//        for (int i = 0; i < 5; i++) {
//            CompletableFuture.supplyAsync(() -> {
//                return roleMapper.listRole();
//            });
//        }
//        Thread.sleep(2000);
        roleMtmResourceManager.lambdaQuery()
                .in(RoleMtmResourcePO::getRoleId, 1, 2)
                .in(RoleMtmResourcePO::getResourceId, 1, 2)
                .list();

    }
}
