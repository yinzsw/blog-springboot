package top.yinzsw.blog.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.yinzsw.blog.model.dto.IpInfoDTO;

/**
 * 百度OpenData API IP 信息查询
 *
 * @author yinzsW
 * @since 22/12/15
 */
@FeignClient(url = "https://opendata.baidu.com", name = "ip")
public interface IpClient {
    @GetMapping("api.php?resource_id=6006&ie=utf8&oe=utf-8&format=json")
    IpInfoDTO getIpInfo(@RequestParam("query") String query);
}
