package top.yinzsw.blog.controller.advice;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.yinzsw.blog.model.vo.ResponseVO;

/**
 * 接口统一响应模型处理
 * <p>
 * 如果Controller方法的返回值类型为 {@link Object} 并且返回值为 {@code null}，则会导致 {@link ResponseBodyAdvice} 失效，导致方法直接返回 {@code null}。
 * </p>
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Slf4j
@RestControllerAdvice(basePackages = "top.yinzsw.blog.controller")
@RequiredArgsConstructor
public class UnifiedResponse implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        var parameterType = returnType.getParameterType();
        var isResponseVO = parameterType.isAssignableFrom(ResponseVO.class);
        var isVoid = parameterType == void.class;
        return !isResponseVO && !isVoid;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ResponseVO<Object> successVO = ResponseVO.success(body);
        return selectedConverterType.isAssignableFrom(StringHttpMessageConverter.class) ? objectMapper.writeValueAsString(successVO) : successVO;
    }
}
