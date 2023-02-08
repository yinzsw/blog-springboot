package top.yinzsw.blog.core.security.jwt;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.util.CommonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * JWT claims数据模型
 *
 * @author yinzsW
 * @since 22/12/25
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class JwtContextDTO {

    /**
     * JWT 过期时间 时间戳(秒)
     */
    private Long exp;

    /**
     * 版本号 ID(雪花id)
     */
    private final Long vid;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * Token 类型
     */
    private TokenTypeEnum type;

    /**
     * Token 签名
     */
    private String sign;

    /**
     * 用户角色列表
     */
    private List<Long> roles;

    public JwtContextDTO() {
        this.vid = IdWorker.getId();
    }

    public JwtContextDTO setType(TokenTypeEnum type) {
        this.exp = Sequence.parseIdTimestamp(this.vid) + type.getTtl();
        this.type = type;
        return this;
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CommonUtils.toDistinctList(roles, role -> new SimpleGrantedAuthority(role.toString()));
    }

    @JsonIgnore
    public Map<String, ?> toMap(ObjectMapper objectMapper) {
        return objectMapper.convertValue(this, new TypeReference<>() {
        });
    }
}
