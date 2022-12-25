package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.client.IpClient;
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

    @Async
    @Override
    public void asyncUpdateUserLoginInfo(Long userId, String userIpAddress) {
        String ipAddress = Optional.ofNullable(userIpAddress).orElseThrow(() -> new BizException("无效的ip地址"));
        String ipSource = Optional.ofNullable(ipClient.getIpInfo(ipAddress).getFirstLocation()).orElse("");

        LambdaUpdateWrapper<UserPO> updateWrapper = new LambdaUpdateWrapper<UserPO>()
                .set(UserPO::getIpAddress, ipAddress)
                .set(UserPO::getIpSource, ipSource)
                .set(UserPO::getLastLoginTime, LocalDateTime.now(ZoneOffset.ofHours(8)))
                .eq(UserPO::getId, userId);

        userMapper.update(null, updateWrapper);
    }
}
