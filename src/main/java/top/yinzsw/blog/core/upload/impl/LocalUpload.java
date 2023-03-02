package top.yinzsw.blog.core.upload.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.core.upload.UploadConfig;
import top.yinzsw.blog.exception.BizException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地长传
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(UploadConfig.class)
@ConditionalOnProperty(prefix = "upload", name = "mode", havingValue = "local")
public class LocalUpload extends AbstractUploadTemplate {
    private final UploadConfig uploadConfig;

    private Path savePath;

    @PostConstruct
    @Override
    public void init() {
        savePath = Paths.get(uploadConfig.getBucket());
        if (Files.notExists(savePath)) {
            try {
                Files.createDirectories(savePath);
            } catch (IOException e) {
                throw new BizException("文件路径创建失败");
            }
        }

        log.info("本地文件上传器, 初始化完毕");
    }

    @Override
    public boolean getIsExists(String filePath) {
        return Files.exists(savePath.resolve(filePath));
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
    public void upload(String filePath, InputStream inputStream) throws IOException {
        Path curSavePath = savePath.resolve(filePath);
        Path parentPath = curSavePath.getParent();
        if (Files.notExists(parentPath)) {
            Files.createDirectories(parentPath);
        }
        Files.copy(inputStream, curSavePath);
    }
}
