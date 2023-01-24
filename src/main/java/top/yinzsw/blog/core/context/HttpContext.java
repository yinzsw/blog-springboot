package top.yinzsw.blog.core.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.model.dto.ContextDTO;
import top.yinzsw.blog.model.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Predicate;

/**
 * HTTP 上下文
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Service
@RequiredArgsConstructor
public class HttpContext {
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final ObjectMapper objectMapper;

    /**
     * <p>
     * <code>X-Forwarded-For</code>: 由Squid开发的字段, 只有在通过了HTTP代理或者负载均衡服务器时才会添加该项, 现在大部分的代理都会加上这个请求头
     * <br/>例子: X-Forwarded-For: client1,proxy1,proxy2; 一般情况下, 第一个ip为客户端真实ip,后面的为经过的代理服务器ip
     * </p>
     * <p>
     * <code>X-Real-IP</code>: Nginx代理一般会加上此请求头
     * </p>
     * <p>
     * <code>Proxy-Client-IP/WL-Proxy-Client-IP</code>: 一般是经过Apache HTTP服务器的请求才会加上的请求头
     * <br/>用Apache HTTP做代理时一般会加上Proxy-Client-IP请求头, 而WL-Proxy-Client-IP是他的WebLogic插件加上的请求头
     * </p>
     *
     * @return 当没能获取到ip返回 {@code null}
     */
    public String getUserIpAddress() {

        Predicate<String> isIp = ip -> StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip);

        String xForwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
        if (isIp.test(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }

        String xRealIp = httpServletRequest.getHeader("X-Real-IP");
        if (isIp.test(xRealIp)) {
            return xRealIp;
        }

        String proxyClientIp = httpServletRequest.getHeader("Proxy-Client-IP");
        if (isIp.test(proxyClientIp)) {
            return proxyClientIp;
        }

        String wlProxyClientIp = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        if (isIp.test(wlProxyClientIp)) {
            return wlProxyClientIp;
        }

        String remoteAddr = httpServletRequest.getRemoteAddr();
        if (isIp.test(remoteAddr)) {
            boolean isLocalIpAddr = "127.0.0.1".equals(remoteAddr) || "0:0:0:0:0:0:0:1".equals(remoteAddr);
            return isLocalIpAddr ? getLocalHost() : remoteAddr;
        }
        return null;
    }

    /**
     * 得到本机ip地址
     *
     * @return 本机ip, 当解析ip失败时返回{@code null}
     */
    private String getLocalHost() {
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
        return localHost.getHostAddress();
    }

    /**
     * 得到当前用户认证信息
     *
     * @return 用户认证信息
     */
    public ContextDTO getCurrentContextDTO() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof ContextDTO) {
            return (ContextDTO) principal;
        }
        throw new BizException(ResponseCodeEnum.UNAUTHENTICATED);
    }

    /**
     * 用户UA
     *
     * @return UA字符串
     */
    public String getUserAgent() {
        return httpServletRequest.getHeader("User-Agent");
    }

    /**
     * 设置响应体信息
     *
     * @param responseBody 响应体内容
     */
    @SneakyThrows
    public void setResponseBody(ResponseVO<?> responseBody) {
        httpServletResponse.setCharacterEncoding("UTF8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonResponseBody = objectMapper.writeValueAsString(responseBody);
        httpServletResponse.getWriter().write(jsonResponseBody);
    }
}
