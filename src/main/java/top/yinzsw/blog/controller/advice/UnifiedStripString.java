package top.yinzsw.blog.controller.advice;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.beans.PropertyEditorSupport;
import java.util.Objects;

/**
 * 去除请求中字符串类型参数的首尾空白字符
 *
 * @author yinzsW
 * @since 23/01/02
 */
@RestControllerAdvice(basePackages = "top.yinzsw.blog.controller")
public class UnifiedStripString extends PropertyEditorSupport {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, this);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String value = Objects.isNull(text) ? null : text.strip();
        setValue(value);
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return Objects.isNull(value) ? "" : value.toString();
    }
}
