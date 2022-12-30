package top.yinzsw.blog.extension.validation.validator;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.extension.validation.MatchFileType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 对{@link MultipartFile}文件类型信息校验实现
 *
 * @author yinzsW
 * @since 22/12/30
 */

public class MultipartFileValidator implements ConstraintValidator<MatchFileType, MultipartFile> {
    private Set<MimeType> mimeTypes;

    @Override
    public void initialize(MatchFileType constraintAnnotation) {
        this.mimeTypes = Arrays.stream(constraintAnnotation.mimeType())
                .map(MimeTypeUtils::parseMimeType)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        String contentType = value.getContentType();
        if (Objects.isNull(contentType)) {
            return false;
        }

        MimeType mimeType = MimeTypeUtils.parseMimeType(contentType);
        return mimeTypes.stream().anyMatch(type -> type.includes(mimeType));
    }
}
