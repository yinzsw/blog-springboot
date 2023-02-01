package top.yinzsw.blog.core.security.jwt;

import org.springframework.security.core.AuthenticationException;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.model.vo.TokenVO;

import java.util.List;

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
    TokenVO createTokenVO(Long userId, List<String> roles);

    /**
     * 解析 http request token信息
     *
     * @param token     token字符串
     * @param isRefresh 是否是{@link TokenTypeEnum#REFRESH}
     * @return token信息
     * @throws AuthenticationException 认证异常
     */
    JwtContextDTO parseAndGetJwtContext(String token, boolean isRefresh) throws AuthenticationException;
}
