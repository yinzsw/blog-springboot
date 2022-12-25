package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import top.yinzsw.blog.model.po.RolePO;

import javax.servlet.http.HttpServletRequest;
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
    private HttpServletRequest request;

    @Test
    void authenticationTest() {
        System.out.println(request);
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
}
