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
    private Long uid;
    private String sign;
    private List<String> roles;
    private TokenTypeEnum type;
}
