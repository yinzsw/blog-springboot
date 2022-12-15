package top.yinzsw.blog.service;

import top.yinzsw.blog.model.vo.ResponseVO;

/**
 * HttpRequest操作接口
 *
 * @author yinzsW
 * @since 22/12/15
 */

public interface HttpService {

    /**
     * 获取用户ip地址
     *
     * @return 用户ip
     */
    String getUserIpAddress();

    /**
     * 异步解析用户ip地址
     *
     * @param ipAddress ip地址
     * @return ip地址信息
     */
    String parseIpSource(String ipAddress);

    /**
     * 为HttpResponse设置响应体
     *
     * @param responseBody 响应体
     */
    void setResponseBody(ResponseVO<?> responseBody);
}
