package top.yinzsw.blog.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.exception.BizException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 文件工具类
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Slf4j
public class FileUtils {
    private static final Set<String> DOCUMENT_SUFFIXES = new HashSet<>() {{
        add(".htm");add(".html");
        add(".txt");add(".text");
        add(".md");
    }};

    /**
     * 获取文件md5值
     *
     * @param inputStream 文件的流
     * @return md5值
     */
    public static String getMd5AsHex(InputStream inputStream) {
        try (inputStream) {
            return DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException e) {
            log.error("getMd5AsHex :{}", e.getMessage());
            throw new BizException("获取文件md5摘要失败");
        }
    }

    /**
     * 得到文件后缀名(扩展名)
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getSuffix(String fileName) {
        return StringUtils.hasText(fileName) ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }

    /**
     * 将{@link MultipartFile}转换{@link File}
     *
     * @param multipartFile multipartFile对象
     * @return file对象
     */
    @SneakyThrows
    public static File toFile(MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        multipartFile.transferTo(file);
        return file;
    }

    public static boolean isDocument(String suffix) {
        return DOCUMENT_SUFFIXES.contains(suffix);
    }
}
