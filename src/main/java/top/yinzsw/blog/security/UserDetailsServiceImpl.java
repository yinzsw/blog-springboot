package top.yinzsw.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.constant.RedisConst;
import top.yinzsw.blog.extension.context.HttpContext;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private final HttpContext httpContext;
    private final UserConverter userConverter;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        UserPO userPO = userService.getUserByNameOrEmail(username);
        Optional.ofNullable(userPO).orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));
        userPO.setIpAddress(httpContext.getUserIpAddress());

        // 用户角色信息
        Long userId = userPO.getId();
        List<String> roles = roleService.getRoleNamesByUserId(userId);

        // 查询用户点赞信息
        Set<Object> likedTalks = redisTemplate.opsForSet().members(RedisConst.USER_LIKED_TALKS_PREFIX + userId);
        Set<Object> likedArticles = redisTemplate.opsForSet().members(RedisConst.USER_LIKED_ARTICLES_PREFIX + userId);
        Set<Object> likedComments = redisTemplate.opsForSet().members(RedisConst.USER_LIKED_COMMENTS_PREFIX + userId);

        userPO.setLastLoginTime(LocalDateTime.now(ZoneOffset.ofHours(8)));
        UserDetailsDTO userDetailsDTO = userConverter.toUserDetailDTO(userPO, roles);
        userDetailsDTO.setTalkLikeSet(likedTalks);
        userDetailsDTO.setArticleLikeSet(likedArticles);
        userDetailsDTO.setCommentLikeSet(likedComments);
        return userDetailsDTO;
    }
}
