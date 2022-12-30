package top.yinzsw.blog.extension.upload.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.upload.UploadConfig;
import top.yinzsw.blog.util.FileUtils;

import java.io.*;
import java.nio.file.Files;

/**
 * 本地长传
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(UploadConfig.class)
@ConditionalOnProperty(prefix = "upload", name = "mode", havingValue = "local")
public class LocalUpload extends AbstractUploadTemplate {
    private final UploadConfig uploadConfig;

    @Override
    public Boolean isExists(String filePath) {
        return new File(uploadConfig.getBucket(), filePath).exists();
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        File directory = new File(uploadConfig.getBucket(), path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BizException("创建目录失败");
            }
        }

        File file = new File(directory.getPath(), fileName);
        boolean isDocument = FileUtils.isDocument(FileUtils.getSuffix(fileName));
        if (isDocument) {
            try (inputStream;
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                while (reader.ready()) {
                    writer.write((char) reader.read());
                }
                writer.flush();
            }
            return;
        }

        try (inputStream;
             BufferedInputStream bis = new BufferedInputStream(inputStream);
             BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()))) {
            byte[] bytes = new byte[2048];
            int length;
            while ((length = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }
            bos.flush();
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(uploadConfig.getDomainUrl())
                .path(uploadConfig.getEndpoint())
                .path("/".concat(filePath)).build();
        return uriComponents.toUriString();
    }
}
