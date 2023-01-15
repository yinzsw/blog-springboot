package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.util.CommonUtils;

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
    private final IpClient ipClient;

    @Override
    public UserPO getUserByNameOrEmail(String identity) {
        if (!StringUtils.hasText(identity)) {
            return null;
        }

        boolean isEmail = CommonUtils.getIsEmail(identity);
        return Db.lambdaQuery(UserPO.class)
                .select(UserPO::getId, UserPO::getUsername, UserPO::getPassword,
                        UserPO::getEmail, UserPO::getNickname, UserPO::getAvatar,
                        UserPO::getIntro, UserPO::getWebSite, UserPO::getIsDisabled)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, identity)
                .one();
    }

    @Override
    public boolean updateUserPassword(String identity, String newPassword) {
        boolean isEmail = CommonUtils.getIsEmail(identity);
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
