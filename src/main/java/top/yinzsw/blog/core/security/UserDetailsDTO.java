package top.yinzsw.blog.core.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.yinzsw.blog.enums.LoginTypeEnum;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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
@Accessors(chain = true)
public class UserDetailsDTO implements UserDetails {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String intro;

    /**
     * 是否禁用
     */
    private Boolean isDisabled;

    /**
     * 用户个人网站
     */
    private String webSite;

    /**
     * 用户登录类型 1默认(邮箱或用户名) 2QQ 3微博
     */
    private LoginTypeEnum loginType;

    /**
     * 上次登录时的IP地址
     */
    private String ipAddress;

    /**
     * IP地址源
     */
    private String ipSource;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 用户角色
     */
    private List<String> roleList;

    /**
     * 点赞说说集合
     */
    private List<Long> likedTalkSet;

    /**
     * 点赞文章集合
     */
    private List<Long> likedArticleSet;

    /**
     * 点赞评论集合
     */
    private List<Long> likedCommentSet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isDisabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return roleList.size() > 0;
    }
}
