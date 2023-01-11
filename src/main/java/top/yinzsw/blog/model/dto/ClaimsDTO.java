package top.yinzsw.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.yinzsw.blog.enums.TokenTypeEnum;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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


    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
