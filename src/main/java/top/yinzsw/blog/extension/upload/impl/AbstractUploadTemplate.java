package top.yinzsw.blog.extension.upload.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.upload.UploadStrategy;
import top.yinzsw.blog.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 抽象上传模板
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Slf4j
public abstract class AbstractUploadTemplate implements UploadStrategy {

    @Override
    public String uploadFile(String path, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = generateFileName(file);
            return uploadFile(path, fileName, inputStream);
        } catch (IOException e) {
            log.error("uploadFile :{}", e.getMessage());
            throw new BizException("文件上传失败");
        }
    }

    @Override
    public String uploadFile(String path, String fileName, InputStream inputStream) {
        try {
            String filePath = UriComponentsBuilder.fromPath(path).path("/".concat(fileName)).build().toUriString();
            if (!isExists(filePath)) {
                upload(path, fileName, inputStream);
            }
            return getFileAccessUrl(filePath);
        } catch (Exception e) {
            log.error("uploadFile :{}", e.getMessage());
            throw new BizException("文件上传失败");
        }
    }

    /**
     * 生成文件名
     *
     * @param metaFile 源文件对象
     * @return 文件名
     */
    public String generateFileName(MultipartFile metaFile) {
        try (InputStream inputStream = metaFile.getInputStream()) {
            String md5 = FileUtils.getMd5AsHex(inputStream);
            String suffix = FileUtils.getSuffix(metaFile.getOriginalFilename());
            return md5.concat(suffix);
        } catch (IOException e) {
            log.error("uploadFile :{}", e.getMessage());
            throw new BizException("文件名生成失败");
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    public abstract Boolean isExists(String filePath);

    /**
     * 上传
     *
     * @param path        路径
     * @param fileName    文件名
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return {@link String}
     */
    public abstract String getFileAccessUrl(String filePath);
}
