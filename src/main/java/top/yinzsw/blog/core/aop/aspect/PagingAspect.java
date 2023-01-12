package top.yinzsw.blog.core.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.model.request.PageReq;

import java.util.Arrays;
import java.util.Optional;

/**
 * 处理分页请求参数的切面类
 *
 * @author yinzsW
 * @since 23/01/10
 */

@Aspect
@Component
public class PagingAspect {

    @Before(value = "top.yinzsw.blog.core.aop.PointCutProvider.isSelfPagingApi()")
    public void beforeAdvice(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .filter(PageReq::instanceOf)
                .map(PageReq::valueOf)
                .findFirst()
                .ifPresent(pageReq -> {
                    pageReq.setPage(Optional.ofNullable(pageReq.getPage()).orElse(1L));
                    pageReq.setSize(Optional.ofNullable(pageReq.getSize()).orElse(10L));
                });
    }
}
