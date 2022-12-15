package top.yinzsw.blog.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import top.yinzsw.blog.enums.ResponseCodeEnum;
import top.yinzsw.blog.model.vo.ResponseVO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger文档配置
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfig {
    private Boolean enable;
    private String title;
    private String version;
    private String description;
    private String serviceUrl;
    private String contactName;
    private String contactUrl;
    private String contactEmail;

    @Bean
    public Docket docket() {
        var apiInfo = apiInfo();
        var responseList = responseList();
        return new Docket(DocumentationType.OAS_30)
                .enable(enable)
                .pathMapping("/")
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, responseList)
                .globalResponses(HttpMethod.POST, responseList)
                .globalResponses(HttpMethod.DELETE, responseList)
                .globalResponses(HttpMethod.PUT, responseList)
                .globalResponses(HttpMethod.PATCH, responseList)
                .genericModelSubstitutes(ResponseVO.class)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 封装接口文档基本信息
     *
     * @return 文档信息对象
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .description(description)
                .termsOfServiceUrl(serviceUrl)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .build();
    }

    /**
     * 根据自定义枚举状态码获取额外响应状态信息
     *
     * @return 响应状态信息列表
     */
    private List<Response> responseList() {
        return Arrays.stream(ResponseCodeEnum.values())
                .map(rce -> new ResponseBuilder()
                        .code(rce.getCode().toString())
                        .description(rce.getDesc())
                        .isDefault(true)
                        .build())
                .collect(Collectors.toList());
    }
}
