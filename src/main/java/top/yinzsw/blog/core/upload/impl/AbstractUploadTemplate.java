package top.yinzsw.blog.core.upload.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.exception.BizException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * 抽象上传模板
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Slf4j
public abstract class AbstractUploadTemplate implements UploadProvider {

    @Override
    public String uploadFile(String path, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = getFileName(file.getOriginalFilename(), inputStream);
            return uploadFile(path, fileName, inputStream);
        } catch (IOException e) {
            log.error("uploadFile :{}", e.getMessage());
            throw new BizException("文件上传失败");
        }
    }

    @Override
    public String uploadFile(String path, String filename, InputStream inputStream) {
        try (inputStream) {
            String filePath = UriComponentsBuilder.fromPath(path).path("/".concat(filename)).build().toUriString();
            if (!getIsExists(filePath)) {
                upload(path, filename, inputStream);
            }
            return getFileAccessUrl(filePath);
        } catch (Exception e) {
            log.error("uploadFile :{}", e.getMessage());
            throw new BizException("文件上传失败");
        }
    }

    /**
     * 获取文件名
     *
     * @param filename    文件路径
     * @param inputStream 文件流
     * @return {@link String} 文件名
     */
    public String getFileName(String filename, InputStream inputStream) throws IOException {
        String md5HexValue = DigestUtils.md5DigestAsHex(inputStream);
        String suffix = Optional.ofNullable(StringUtils.getFilenameExtension(filename))
                .map("."::concat).orElse("");
        return md5HexValue.concat(suffix);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@link Boolean} 是否存在
     */
    public abstract Boolean getIsExists(String filePath);

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return {@link String} 文件资源路径
     */
    public abstract String getFileAccessUrl(String filePath);

    /**
     * 文件上传
     *
     * @param path        路径
     * @param filename    文件名
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    public abstract void upload(String path, String filename, InputStream inputStream) throws IOException;
}
