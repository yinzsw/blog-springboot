package top.yinzsw.blog.core.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 切入点提供器
 *
 * @author yinzsW
 * @since 23/01/10
 */

public interface PointCutProvider {
    /**
     * 判断是不是 REST-ful 控制器
     */
    @Pointcut("@target(org.springframework.web.bind.annotation.RestController)")
    default void isRestController() {
    }

    /**
     * 判断是否是自己定义的 REST-ful控制器
     */
    @Pointcut("execution(public * top.yinzsw.blog.controller..*(..))) && isRestController()")
    default void isSelfRestController() {
    }

    /**
     * 判断方法是否有 <code>@org.springframework.web.bind.annotation.*Mapping</code> 注解
     */
    @Pointcut("" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.GetMapping)     ||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)    ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)     ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)  ||" +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    default void canMapping() {
    }

    /**
     * 判断是不是自己定义的接口
     */
    @Pointcut("isSelfRestController() && canMapping()")
    default void isSelfApi() {
    }

    /**
     * 判断接口是不是自己定义的分页接口
     */
    @Pointcut("isSelfApi() && execution(* *(..,top.yinzsw.blog.model.request.PageReq,..)))")
    default void isSelfPagingApi() {
    }
}
