package top.yinzsw.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import lombok.Data;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yinzsw.blog.enums.ResponseCodeEnum;

import java.util.Objects;

/**
 * Open Api 文档配置
 *
 * @author yinzsW
 * @since 22/12/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "openapi")
public class OpenApiConfig {
    private String title;
    private String version;
    private String description;
    private String contactName;
    private String contactEmail;
    private String contactUrl;

    @Bean
    public OpenAPI openAPI() {
        var contact = new Contact().name(contactName).email(contactEmail).url(contactUrl);
        var info = new Info().title(title).version(version).description(description).contact(contact);
        return new OpenAPI().info(info);
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            Content content = operation.getResponses().get("200").getContent();
            if (Objects.nonNull(content)) {
                content.keySet().forEach(s -> {
                    MediaType mediaType = content.get(s);
                    mediaType.schema(wrapperSchema(mediaType.getSchema()));
                });
            }
            return operation;
        };
    }

    private Schema<?> wrapperSchema(final Schema<?> schema) {
        Schema<?> wrapperSchema = new Schema<>();
        wrapperSchema.addProperty("code", new IntegerSchema()._default(ResponseCodeEnum.SUCCESS.getCode()).title("状态码"));
        wrapperSchema.addProperty("msg", new StringSchema()._default(ResponseCodeEnum.SUCCESS.getDesc()).title("信息"));
        wrapperSchema.addProperty("data", schema._default(null).title("数据"));
        return wrapperSchema;
    }
}
