package top.yinzsw.blog.extension.upload;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 上传文件配置类
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Data
@Configuration
@ConfigurationProperties("upload")
public class UploadConfig {

    /**
     * 上传模式
     */
    private UploadModeEnum mode;

    /**
     * bucket
     * 当本地模式时,为上传路径
     */
    private String bucket;

    /**
     * endpoint
     * 当本地模式时,为域名的一级uri
     */
    private String endpoint;

    /**
     * domainUrl
     */
    private String domainUrl;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;
}
