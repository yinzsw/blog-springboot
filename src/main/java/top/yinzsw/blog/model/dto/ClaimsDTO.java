package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yinzsw.blog.enums.TokenTypeEnum;

import java.util.List;

/**
 * JWT claims数据模型
 *
 * @author yinzsW
 * @since 22/12/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimsDTO {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 访问的资源id
     */
    private Long rid;

    /**
     * token签名
     */
    private String sign;

    /**
     * 用户角色列表
     */
    private List<String> roles;

    /**
     * token类型
     */
    private TokenTypeEnum type;
}
