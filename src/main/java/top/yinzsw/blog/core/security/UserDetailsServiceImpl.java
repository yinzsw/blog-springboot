package top.yinzsw.blog.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.service.RoleService;

import java.util.List;
import java.util.Optional;

/**
 * 用户详细信息服务
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {
    private final UserManager userManager;
    private final RoleService roleService;
    private final UserConverter userConverter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        UserPO userPO = userManager.getUserByNameOrEmail(username);
        Optional.ofNullable(userPO).orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));

        // 用户角色信息
        Long userId = userPO.getId();
        List<Long> roleIds = roleService.getRoleIdsByUserId(userId);
        return userConverter.toUserDetailDTO(userPO, roleIds);
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        boolean status = userManager.updateUserPassword(user.getUsername(), newPassword);
        if (status) {
            UserDetailsDTO userDetailsDTO = (UserDetailsDTO) user;
            userDetailsDTO.setPassword(newPassword);
        }
        return user;
    }
}
