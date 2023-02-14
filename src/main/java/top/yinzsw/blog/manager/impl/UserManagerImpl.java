package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.constant.MQConst;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.dto.EmailCodeDTO;
import top.yinzsw.blog.model.po.HistoryPO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.UUID;

/**
 * 用户通用业务处理层实现
 *
 * @author yinzsW
 * @since 22/12/25
 */
@Service
@RequiredArgsConstructor
public class UserManagerImpl extends ServiceImpl<UserMapper, UserPO> implements UserManager {
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final IpClient ipClient;

    @Override
    public void saveEmailVerificationCode(String email, String code) {
        String emailCodeKey = RedisConstEnum.EMAIL_CODE.getKey(email);
        stringRedisTemplate.opsForValue().set(emailCodeKey, code, USER_EMAIL_CODE_EXPIRE_TIME);
    }

    @Override
    public void checkEmailVerificationCode(String email, String code) throws BizException {
        String emailCodeKey = RedisConstEnum.EMAIL_CODE.getKey(email);
        String emailCode = stringRedisTemplate.opsForValue().get(emailCodeKey);
        if (!code.equalsIgnoreCase(emailCode)) {
            throw new BizException("邮箱验证码错误");
        }
        stringRedisTemplate.delete(emailCodeKey);
    }

    @Override
    public String sendEmailCode(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        EmailCodeDTO emailCodeDTO = new EmailCodeDTO(email, code, USER_EMAIL_CODE_EXPIRE_TIME.toMinutes());
        rabbitTemplate.convertAndSend(MQConst.EMAIL_EXCHANGE, MQConst.EMAIL_CODE_KEY, emailCodeDTO);
        return code;
    }

    @Override
    public UserPO getUserByNameOrEmail(String identity) {
        if (!StringUtils.hasText(identity)) {
            return null;
        }

        boolean isEmail = VerifyUtils.isEmail(identity);
        return Db.lambdaQuery(UserPO.class)
                .select(UserPO::getId, UserPO::getUsername, UserPO::getPassword,
                        UserPO::getEmail, UserPO::getNickname, UserPO::getAvatar,
                        UserPO::getIntro, UserPO::getWebSite, UserPO::getIsDisabled)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, identity)
                .one();
    }

    @Override
    public boolean updateUserPassword(String identity, String newPassword) {
        boolean isEmail = VerifyUtils.isEmail(identity);
        return Db.lambdaUpdate(UserPO.class)
                .set(UserPO::getPassword, newPassword)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, identity)
                .update();
    }

    @Async
    @Override
    public void saveUserLoginHistory(Long userId, String userAgent, String ipAddress) {
        String ipLocation = ipClient.getIpInfo(ipAddress).getFirstLocation().orElse("");
        HistoryPO historyPO = new HistoryPO()
                .setUserId(userId)
                .setIpAddress(ipAddress)
                .setIpSource(ipLocation)
                .setUserAgent(userAgent);
        Db.save(historyPO);
    }
}
