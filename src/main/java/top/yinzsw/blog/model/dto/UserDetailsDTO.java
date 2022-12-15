package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.yinzsw.blog.model.po.UserPO;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户详细信息
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO implements UserDetails {

    /**
     * 用户信息
     */
    private UserPO userPO;

    /**
     * 用户角色
     */
    private List<String> roleList;

    /**
     * 点赞说说集合
     */
    private Set<Object> talkLikeSet;

    /**
     * 点赞文章集合
     */
    private Set<Object> articleLikeSet;

    /**
     * 点赞评论集合
     */
    private Set<Object> commentLikeSet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.userPO.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userPO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.userPO.getIsDisabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
