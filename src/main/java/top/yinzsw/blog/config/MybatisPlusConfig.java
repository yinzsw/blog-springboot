package top.yinzsw.blog.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus配置
 *
 * @author yinzsW
 * @since 2022/12/15
 */
@Configuration
@MapperScan("top.yinzsw.blog.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(getPaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * mybatis分页拦截器
     *
     * @return 拦截器对象
     */
    private InnerInterceptor getPaginationInnerInterceptor() {
        var pagination = new PaginationInnerInterceptor();
        pagination.setDbType(DbType.MYSQL);
        pagination.setMaxLimit(30L);
        return pagination;
    }
}
