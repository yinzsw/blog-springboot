package top.yinzsw.blog.extension.mybatisplus;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Slf4j
@SpringBootTest
public class MybatisPlusTests {

    @Test
    void test() {
//        LambdaQueryWrapper<RoleMtmMenuPO> in = Wrappers.<RoleMtmMenuPO>lambdaQuery()
//                .in(RoleMtmMenuPO::getRoleId, 1);
//        Map<Long, Long> map = SimpleQuery.map(in, RoleMtmMenuPO::getRoleId, RoleMtmMenuPO::getMenuId);
//        System.out.println(map);
//        List<RoleMtmMenuPO> list = Db.lambdaQuery(RoleMtmMenuPO.class)
//                .select(RoleMtmMenuPO::getRoleId, RoleMtmMenuPO::getMenuIdc)
//                .in(RoleMtmMenuPO::getRoleId, 1)
//                .groupBy(RoleMtmMenuPO::getRoleId)
//                .list();
//        System.out.println(list);
//        LambdaQueryWrapper<ArticlePO> select = Wrappers.lambdaQuery(ArticlePO.class)
//                .select(ArticlePO::getId)
//                .func(articlePOLambdaQueryWrapper -> articlePOLambdaQueryWrapper.select(ArticlePO::getCreateTime));
//
//        Db.list(select);

//        log.info(aa.getSqlComment());
//        log.info(aa.getSqlFirst());
//        log.info(aa.getSqlSet());
//        log.info(aa.getSqlSelect());
//        log.info(aa.getTargetSql());
//        log.info(aa.getSqlSegment());
//        log.info(aa.getCustomSqlSegment());
    }
}
