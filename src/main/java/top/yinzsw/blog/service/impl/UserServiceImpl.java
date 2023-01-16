package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.RedisManager;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.PasswordByEmailReq;
import top.yinzsw.blog.model.request.PasswordByOldReq;
import top.yinzsw.blog.model.request.UserInfoReq;
import top.yinzsw.blog.service.UserService;

import java.util.Optional;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:14:31
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {
    private final UserManager userManager;
    private final RedisManager redisManager;
    private final HttpContext httpContext;
    private final UploadProvider uploadProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean updateUserInfo(UserInfoReq userInfoReq) {
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        UserPO userPO = new UserPO()
                .setNickname(userInfoReq.getNickname())
                .setIntro(userInfoReq.getIntro())
                .setWebSite(userInfoReq.getWebSite());
        return lambdaUpdate().eq(UserPO::getId, uid).update(userPO);
    }

    @Override
    public String updateUserAvatar(MultipartFile file) {
        String avatarUrl = uploadProvider.uploadFile(FilePathEnum.AVATAR.getPath(), file);
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        lambdaUpdate().set(UserPO::getAvatar, avatarUrl).eq(UserPO::getId, uid).update();
        return avatarUrl;
    }

    @Override
    public boolean updateUserEmail(String email, String code) {
        redisManager.checkEmailVerificationCode(email, code);
        Long uid = httpContext.getCurrentClaimsDTO().getUid();
        return lambdaUpdate().set(UserPO::getEmail, email).eq(UserPO::getId, uid).update();
    }

    @Override
    public boolean updateUserIsDisable(Long userId, Boolean isDisabled) {
        return lambdaUpdate().set(UserPO::getIsDisabled, isDisabled).eq(UserPO::getId, userId).update();
    }

    @Override
    public boolean updateUserPassword(PasswordByEmailReq password) {
        Optional.ofNullable(userManager.getUserByNameOrEmail(password.getEmail()))
                .orElseThrow(() -> new BizException("邮箱尚未注册"));

        redisManager.checkEmailVerificationCode(password.getEmail(), password.getCode());
        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return userManager.updateUserPassword(password.getEmail(), newPassword);
    }

    @Override
    public boolean updateUserPassword(PasswordByOldReq password) {
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        String oldPassword = lambdaQuery().select(UserPO::getPassword).eq(UserPO::getId, uid).one().getPassword();
        boolean notMatches = !passwordEncoder.matches(password.getOldPassword(), oldPassword);
        if (notMatches) {
            throw new BizException("旧密码不正确");
        }

        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return lambdaUpdate().set(UserPO::getPassword, newPassword).eq(UserPO::getId, uid).update();
    }
}




