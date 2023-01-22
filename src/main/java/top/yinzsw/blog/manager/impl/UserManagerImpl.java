package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.dto.UserLikedDTO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.util.VerifyUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;

/**
 * 用户通用业务处理层实现
 *
 * @author yinzsW
 * @since 22/12/25
 */
@Service
@RequiredArgsConstructor
public class UserManagerImpl implements UserManager {
    private final IpClient ipClient;
    private final RedisTemplate<String, ?> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveEmailVerificationCode(String email, String code) {
        String emailCodeKey = USER_EMAIL_CODE_PREFIX + email;
        stringRedisTemplate.opsForValue().set(emailCodeKey, code, Duration.ofMinutes(USER_EMAIL_CODE_EXPIRE_TIME));
    }

    @Override
    public void checkEmailVerificationCode(String email, String code) throws BizException {
        String emailCodeKey = USER_EMAIL_CODE_PREFIX + email;
        String emailCode = stringRedisTemplate.opsForValue().get(emailCodeKey);
        if (!code.equalsIgnoreCase(emailCode)) {
            throw new BizException("邮箱验证码错误");
        }
        stringRedisTemplate.delete(emailCodeKey);
    }

    @Override
    public UserLikedDTO getUserLikeInfo(Long userId) {
        @SuppressWarnings("unchecked")
        SetOperations<String, Long> opsForSet = (SetOperations<String, Long>) redisTemplate.opsForSet();
        Set<Long> likedTalks = opsForSet.members(USER_LIKED_TALKS_PREFIX + userId);
        Set<Long> likedArticles = opsForSet.members(USER_LIKED_ARTICLES_PREFIX + userId);
        Set<Long> likedComments = opsForSet.members(USER_LIKED_COMMENTS_PREFIX + userId);
        return new UserLikedDTO()
                .setLikedTalkSet(likedTalks)
                .setLikedArticleSet(likedArticles)
                .setLikedCommentSet(likedComments);
    }

    @Override
    public UserPO getUserByNameOrEmail(String identity) {
        if (!StringUtils.hasText(identity)) {
            return null;
        }

        boolean isEmail = VerifyUtils.getIsEmail(identity);
        return Db.lambdaQuery(UserPO.class)
                .select(UserPO::getId, UserPO::getUsername, UserPO::getPassword,
                        UserPO::getEmail, UserPO::getNickname, UserPO::getAvatar,
                        UserPO::getIntro, UserPO::getWebSite, UserPO::getIsDisabled)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, identity)
                .one();
    }

    @Override
    public boolean updateUserPassword(String identity, String newPassword) {
        boolean isEmail = VerifyUtils.getIsEmail(identity);
        return Db.lambdaUpdate(UserPO.class)
                .set(UserPO::getPassword, newPassword)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, identity)
                .update();
    }

    @Async
    @Override
    public void updateUserLoginInfo(Long userId, String userIpAddress, LocalDateTime lastLoginTime) {
        String ipAddress = Optional.ofNullable(userIpAddress).orElseThrow(() -> new BizException("无效的ip地址"));
        String ipSource = Optional.ofNullable(ipClient.getIpInfo(ipAddress).getFirstLocation()).orElse("");
        LocalDateTime loginTime = Optional.ofNullable(lastLoginTime).orElse(LocalDateTime.now(ZoneOffset.ofHours(8)));

        UserPO userPO = new UserPO()
                .setIpAddress(ipAddress)
                .setIpSource(ipSource)
                .setLastLoginTime(loginTime);
        Db.lambdaUpdate(UserPO.class).eq(UserPO::getId, userId).update(userPO);
    }
}
