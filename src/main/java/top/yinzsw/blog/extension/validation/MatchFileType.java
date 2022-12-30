package top.yinzsw.blog.extension.validation;

import top.yinzsw.blog.extension.validation.validator.MultipartFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 匹配上传文件类型信息
 *
 * @author yinzsW
 * @since 22/12/30
 */
@Documented
@Repeatable(MatchFileType.List.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = MultipartFileValidator.class)
public @interface MatchFileType {

    String message() default "not supported file type";

    String[] mimeType() default "*/*";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @interface List {
        MatchFileType[] value();
    }
}
