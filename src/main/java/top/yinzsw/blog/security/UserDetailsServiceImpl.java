package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.constant.RedisConst;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.service.UserService;
import top.yinzsw.blog.util.HttpUtil;
import top.yinzsw.blog.util.RedisUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 用户详细信息服务
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;
    private final HttpUtil httpUtil;
    private final RedisUtil redisUtil;
    private final UserConverter userConverter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        UserPO userPO = userService.getUserByNameOrEmail(username);
        Optional.ofNullable(userPO).orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));
        userPO.setIpAddress(httpUtil.getUserIpAddress());

        // 用户角色信息
        Long userId = userPO.getId();
        List<String> roles = roleService.getRoleNamesByUserId(userId);

        // 查询用户点赞信息
        Set<Object> likedTalks = redisUtil.sMembers(RedisConst.USER_LIKED_TALKS_PREFIX + userId);
        Set<Object> likedArticles = redisUtil.sMembers(RedisConst.USER_LIKED_ARTICLES_PREFIX + userId);
        Set<Object> likedComments = redisUtil.sMembers(RedisConst.USER_LIKED_COMMENTS_PREFIX + userId);

        UserDetailsDTO userDetailsDTO = userConverter.toUserDetailDTO(userPO, roles);
        userDetailsDTO.setTalkLikeSet(likedTalks);
        userDetailsDTO.setArticleLikeSet(likedArticles);
        userDetailsDTO.setCommentLikeSet(likedComments);
        return userDetailsDTO;
    }
}
