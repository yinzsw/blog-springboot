package top.yinzsw.blog.core.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.model.vo.TokenVO;

import java.util.List;
import java.util.Optional;

/**
 * Json Web Token 操作接口
 *
 * @author yinzsW
 * @since 22/12/21
 */

public interface JwtManager {

    /**
     * 根据用户id创建access token 与 refresh token
     *
     * @param userId 用户id
     * @param roles  角色列表
     * @return token信息
     */
    TokenVO createTokenVO(Long userId, List<Long> roles);

    /**
     * 解析 http request token信息
     *
     * @param isRefresh 是否是{@link TokenTypeEnum#REFRESH}
     * @return token信息
     * @throws AuthenticationException 认证异常
     */
    JwtContextDTO parseAndGetJwtContext(boolean isRefresh) throws AuthenticationException;

    /**
     * 添加禁用用户凭证
     *
     * @return 是否成功
     */
    boolean blockUserToken();

    /**
     * 得到当前用户认证信息上下文
     *
     * @return 用户认证信息上下文
     */
    static Optional<JwtContextDTO> getCurrentContextDTO() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JwtContextDTO jwtContextDTO = principal instanceof JwtContextDTO ? (JwtContextDTO) principal : null;
        return Optional.ofNullable(jwtContextDTO);
    }

    /**
     * 获取当前资源id
     *
     * @return 资源id
     */
    static long getCurrentResourceId() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long resourceId = details instanceof Long ? (Long) details : null;
        return Optional.ofNullable(resourceId).orElse(0L);
    }
}
