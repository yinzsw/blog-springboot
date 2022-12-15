package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import top.yinzsw.blog.constant.RedisConst;
import top.yinzsw.blog.model.dto.UserDetailsDTO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.service.HttpService;
import top.yinzsw.blog.service.RedisService;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.service.UserService;
import top.yinzsw.blog.mapper.UserMapper;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:14:31
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {
    private static final Pattern EMAIL_REGEXP = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    private final HttpService httpService;
    private final RedisService redisService;
    private final RoleService roleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isNullOrBlank(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        boolean isEmail = EMAIL_REGEXP.matcher(username).find();
        UserPO userPO = getOne(new LambdaQueryWrapper<UserPO>()
                .select(UserPO::getId, UserPO::getUsername, UserPO::getPassword,
                        UserPO::getEmail, UserPO::getNickname, UserPO::getAvatar,
                        UserPO::getIntro, UserPO::getWebSite, UserPO::getIsDisabled,
                        UserPO::getLoginType, UserPO::getLastLoginTime)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, username));
        if (Objects.isNull(userPO)) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 用户角色信息
        Long userId = userPO.getId();
        List<String> roles = roleService.getRoleNamesByUserId(userId);

        // 查询用户点赞信息
        Set<Object> likedTalks = redisService.sMembers(RedisConst.USER_LIKED_TALKS_PREFIX + userId);
        Set<Object> likedArticles = redisService.sMembers(RedisConst.USER_LIKED_ARTICLES_PREFIX + userId);
        Set<Object> likedComments = redisService.sMembers(RedisConst.USER_LIKED_COMMENTS_PREFIX + userId);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserPO(userPO);
        userDetailsDTO.setRoleList(roles);
        userDetailsDTO.setTalkLikeSet(likedTalks);
        userDetailsDTO.setArticleLikeSet(likedArticles);
        userDetailsDTO.setCommentLikeSet(likedComments);
        return userDetailsDTO;
    }

    @Async
    @Override
    public void updateLoginInfo(UserPO userPO) {
        String ipAddress = Optional.ofNullable(userPO.getIpAddress()).orElse("");
        String ipSource = Optional.ofNullable(httpService.parseIpSource(ipAddress)).orElse("");

        LambdaUpdateWrapper<UserPO> updateWrapper = new LambdaUpdateWrapper<UserPO>()
                .set(UserPO::getIpAddress, ipAddress)
                .set(UserPO::getIpSource, ipSource)
                .set(UserPO::getLastLoginTime, LocalDateTime.now(ZoneOffset.ofHours(8)))
                .eq(UserPO::getId, userPO.getId());
        update(updateWrapper);
    }
}




