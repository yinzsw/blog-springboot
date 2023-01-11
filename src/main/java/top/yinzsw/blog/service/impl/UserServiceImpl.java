package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.upload.UploadStrategy;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.PasswordByEmailReq;
import top.yinzsw.blog.model.request.PasswordByOldReq;
import top.yinzsw.blog.model.request.UserInfoReq;
import top.yinzsw.blog.service.UserService;
import top.yinzsw.blog.util.CommonUtils;

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
    private final HttpContext httpContext;
    private final UploadStrategy uploadStrategy;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPO getUserByNameOrEmail(String identity) {
        if (!StringUtils.hasText(identity)) {
            return null;
        }
        boolean email = CommonUtils.isEmail(identity);
        return lambdaQuery()
                .select(UserPO::getId, UserPO::getUsername, UserPO::getPassword,
                        UserPO::getEmail, UserPO::getNickname, UserPO::getAvatar,
                        UserPO::getIntro, UserPO::getWebSite, UserPO::getIsDisabled)
                .eq(email ? UserPO::getEmail : UserPO::getUsername, identity)
                .one();
    }

    @Override
    public Boolean updateUserInfo(UserInfoReq userInfoReq) {
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        return lambdaUpdate()
                .set(UserPO::getNickname, userInfoReq.getNickname())
                .set(UserPO::getIntro, userInfoReq.getIntro())
                .set(UserPO::getWebSite, userInfoReq.getWebSite())
                .eq(UserPO::getId, uid)
                .update();
    }

    @Override
    public String updateUserAvatar(MultipartFile file) {
        String avatarUrl = uploadStrategy.uploadFile(FilePathEnum.AVATAR.getPath(), file);
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        lambdaUpdate().set(UserPO::getAvatar, avatarUrl).eq(UserPO::getId, uid).update();
        return avatarUrl;
    }

    @Override
    public Boolean updateUserEmail(String email, String code) {
        userManager.checkEmailVerificationCode(email, code);
        Long uid = httpContext.getCurrentClaimsDTO().getUid();
        return lambdaUpdate().set(UserPO::getEmail, email).eq(UserPO::getId, uid).update();
    }

    @Override
    public Boolean updateUserDisable(Long userId, Boolean disable) {
        return lambdaUpdate().set(UserPO::getIsDisabled, disable).eq(UserPO::getId, userId).update();
    }

    @Override
    public Boolean updateUserPassword(String identity, String newPassword) {
        boolean email = CommonUtils.isEmail(identity);
        return lambdaUpdate()
                .set(UserPO::getPassword, newPassword)
                .eq(email ? UserPO::getEmail : UserPO::getUsername, identity)
                .update();
    }

    @Override
    public Boolean updateUserPassword(PasswordByEmailReq password) {
        Optional.ofNullable(getUserByNameOrEmail(password.getEmail()))
                .orElseThrow(() -> new BizException("邮箱尚未注册"));

        userManager.checkEmailVerificationCode(password.getEmail(), password.getCode());
        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return updateUserPassword(password.getEmail(), newPassword);
    }

    @Override
    public Boolean updateUserPassword(PasswordByOldReq password) {
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        String oldPassword = lambdaQuery()
                .select(UserPO::getPassword)
                .eq(UserPO::getId, uid)
                .one()
                .getPassword();

        boolean notMatches = !passwordEncoder.matches(password.getOldPassword(), oldPassword);
        if (notMatches) {
            throw new BizException("旧密码不正确");
        }

        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return lambdaUpdate().set(UserPO::getPassword, newPassword).eq(UserPO::getId, uid).update();
    }
}




