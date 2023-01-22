package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.dto.UserLikedDTO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.service.RoleService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private final HttpContext httpContext;
    private final UserConverter userConverter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        UserPO userPO = userManager.getUserByNameOrEmail(username);
        Optional.ofNullable(userPO).orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));
        userPO.setIpAddress(httpContext.getUserIpAddress());
        userPO.setLastLoginTime(LocalDateTime.now(ZoneOffset.ofHours(8)));

        // 用户角色信息
        Long userId = userPO.getId();
        List<String> roleList = roleService.getRoleNamesByUserId(userId);

        // 查询用户点赞信息
        UserLikedDTO userLikedDTO = userManager.getUserLikeInfo(userId);
        return userConverter.toUserDetailDTO(userPO, roleList, userLikedDTO);
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
