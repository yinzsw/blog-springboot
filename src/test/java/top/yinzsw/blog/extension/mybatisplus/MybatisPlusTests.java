package top.yinzsw.blog.extension.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.model.po.ArticlePO;

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
        LambdaQueryWrapper<ArticlePO> select = Wrappers.lambdaQuery(ArticlePO.class)
                .select(ArticlePO::getId)
                .func(articlePOLambdaQueryWrapper -> articlePOLambdaQueryWrapper.select(ArticlePO::getCreateTime));

        Db.list(select);

//        log.info(aa.getSqlComment());
//        log.info(aa.getSqlFirst());
//        log.info(aa.getSqlSet());
//        log.info(aa.getSqlSelect());
//        log.info(aa.getTargetSql());
//        log.info(aa.getSqlSegment());
//        log.info(aa.getCustomSqlSegment());
    }
}
