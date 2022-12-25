package top.yinzsw.blog.util;

import top.yinzsw.blog.model.dto.ClaimsDTO;
import top.yinzsw.blog.model.vo.ResponseVO;

/**
 * HttpRequest操作接口
 *
 * @author yinzsW
 * @since 22/12/15
 */

public interface HttpUtil {

    /**
     * 获取用户ip地址
     *
     * @return 用户ip
     */
    String getUserIpAddress();

    /**
     * 为HttpResponse设置响应体
     *
     * @param responseBody 响应体
     */
    void setResponseBody(ResponseVO<?> responseBody);

    /**
     * 获取用户代理
     *
     * @return 用户代理信息
     */
    String getUserAgent();

    /**
     * 得到当前用户认证信息
     *
     * @return 用户认证信息
     */
    ClaimsDTO getCurrentClaimsDTO();
}
