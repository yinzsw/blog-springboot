package top.yinzsw.blog.util;

import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.model.dto.ClaimsDTO;
import top.yinzsw.blog.model.vo.TokenVO;

import java.util.List;

/**
 * Json Web Token 操作接口
 *
 * @author yinzsW
 * @since 22/12/21
 */

public interface JwtUtil {

    /**
     * 根据用户id创建access token 与 refresh token
     *
     * @param userId 用户id
     * @param roles  角色列表
     * @return token信息
     */
    TokenVO createTokenVO(Long userId, List<String> roles);

    /**
     * 根据token返回用户id
     *
     * @param token           token字符串
     * @param expectTokenType 期待的token类型
     * @return token声明信息
     * @throws BizException 业务异常
     */
    ClaimsDTO parseTokenInfo(String token, TokenTypeEnum expectTokenType) throws BizException;
}
