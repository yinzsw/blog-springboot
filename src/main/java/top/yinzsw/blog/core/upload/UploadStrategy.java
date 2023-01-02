package top.yinzsw.blog.core.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 定义文件上传策略接口
 *
 * @author yinzsW
 * @since 22/12/29
 */

public interface UploadStrategy {

    /**
     * 上传文件
     *
     * @param path 上传路径
     * @param file 文件
     * @return {@link String} 文件地址
     */
    String uploadFile(String path, MultipartFile file);

    /**
     * 上传文件
     *
     * @param path        路径
     * @param fileName    文件名
     * @param inputStream 输入流
     * @return {@link String} 文件路径
     */
    String uploadFile(String path, String fileName, InputStream inputStream);
}
