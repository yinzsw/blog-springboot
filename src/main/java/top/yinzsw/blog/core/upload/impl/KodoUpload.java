package top.yinzsw.blog.core.upload.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.core.upload.UploadConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 七牛云储存(Kodo)上传
 *
 * @author yinzsW
 * @since 23/02/08
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(UploadConfig.class)
@ConditionalOnProperty(prefix = "upload", name = "mode", havingValue = "kodo")
public class KodoUpload extends AbstractUploadTemplate {
    private final UploadConfig uploadConfig;

    private String uploadToken;
    private UploadManager uploadManager;
    private BucketManager bucketManager;

    @PostConstruct
    @Override
    public void init() {
        Auth auth = Auth.create(uploadConfig.getAccessKey(), uploadConfig.getAccessKeySecret());
        uploadToken = auth.uploadToken(uploadConfig.getBucket());

        Configuration configuration = new Configuration(Region.huanan());
        configuration.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        uploadManager = new UploadManager(configuration);
        bucketManager = new BucketManager(auth, configuration);

        log.info("七牛云Kodo初始化完毕");
    }

    @Override
    public boolean getIsExists(String filePath) {
        try {
            if (Objects.isNull(bucketManager.stat(uploadConfig.getBucket(), filePath))) {
                return false;
            }
        } catch (QiniuException e) {
            return false;
        }
        return true;
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(uploadConfig.getDomainUrl())
                .path("/".concat(filePath)).build();
        return uriComponents.toUriString();
    }

    @Override
    public void upload(String filePath, InputStream inputStream) throws IOException {
        uploadManager.put(inputStream, filePath, uploadToken, null, null);
    }
}
