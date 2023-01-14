package top.yinzsw.blog.extension.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.manager.RoleMtmResourceManager;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/01/14
 */
@Slf4j
@SpringBootTest
public class MybatisTests {
    private @Autowired RoleMtmResourceManager roleMtmResourceManager;

    @Test
    void SimpleTest() {
        var eq = new LambdaQueryWrapper<RoleMtmResourcePO>().eq(RoleMtmResourcePO::getResourceId, 1L);
        System.out.println(eq.getSqlSelect());
        System.out.println(eq.getTargetSql());
        System.out.println(eq.getSqlSet());
        System.out.println(eq.getSqlFirst());
        System.out.println(eq.getSqlComment());
        System.out.println(eq.getSqlSegment());
        System.out.println(eq.getCustomSqlSegment());

        String s = PropertyNamer.methodToProperty(LambdaUtils.extract(RoleMtmResourcePO::getResourceId).getImplMethodName());
        System.out.println(s);

    }
}
