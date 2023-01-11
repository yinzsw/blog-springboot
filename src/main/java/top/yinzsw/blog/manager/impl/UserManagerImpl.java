package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.constant.RedisConst;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.po.UserPO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * 用户通用业务处理层实现
 *
 * @author yinzsW
 * @since 22/12/25
 */
@Service
@RequiredArgsConstructor
public class UserManagerImpl implements UserManager {
    private final UserMapper userMapper;
    private final IpClient ipClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Async
    @Override
    public void asyncUpdateUserLoginInfo(Long userId, String userIpAddress, LocalDateTime lastLoginTime) {
        String ipAddress = Optional.ofNullable(userIpAddress).orElseThrow(() -> new BizException("无效的ip地址"));
        String ipSource = Optional.ofNullable(ipClient.getIpInfo(ipAddress).getFirstLocation()).orElse("");
        LocalDateTime loginTime = Optional.ofNullable(lastLoginTime).orElse(LocalDateTime.now(ZoneOffset.ofHours(8)));

        LambdaUpdateWrapper<UserPO> updateWrapper = Wrappers.lambdaUpdate(UserPO.class)
                .set(UserPO::getIpAddress, ipAddress)
                .set(UserPO::getIpSource, ipSource)
                .set(UserPO::getLastLoginTime, loginTime)
                .eq(UserPO::getId, userId);
        userMapper.update(null, updateWrapper);
    }

    @Override
    public void checkEmailVerificationCode(String email, String code) throws BizException {
        String redisEmailCodeKey = RedisConst.USER_EMAIL_CODE_PREFIX + email;
        if (!code.equalsIgnoreCase(stringRedisTemplate.opsForValue().get(redisEmailCodeKey))) {
            throw new BizException("邮箱验证码错误");
        }
        stringRedisTemplate.delete(redisEmailCodeKey);
    }
}
