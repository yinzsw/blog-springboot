package top.yinzsw.blog.core.upload.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.core.upload.UploadConfig;
import top.yinzsw.blog.exception.BizException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 本地长传
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Service
@RequiredArgsConstructor
@ConditionalOnBean(UploadConfig.class)
@ConditionalOnProperty(prefix = "upload", name = "mode", havingValue = "local")
public class LocalUpload extends AbstractUploadTemplate {
    private final UploadConfig uploadConfig;

    @Override
    public Boolean getIsExists(String filePath) {
        return new File(uploadConfig.getBucket(), filePath).exists();
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(uploadConfig.getDomainUrl())
                .path(uploadConfig.getEndpoint())
                .path("/".concat(filePath)).build();
        return uriComponents.toUriString();
    }

    @Override
    public void upload(String path, String filename, InputStream inputStream) throws IOException {
        File directory = new File(uploadConfig.getBucket(), path);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new BizException("创建目录失败");
        }

        Files.copy(inputStream, Path.of(directory.getPath(), filename));
    }
}
