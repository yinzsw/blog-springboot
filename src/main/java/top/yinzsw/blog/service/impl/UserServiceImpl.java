package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.po.UserMtmRolePO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.PasswordByEmailReq;
import top.yinzsw.blog.model.request.PasswordByOldReq;
import top.yinzsw.blog.model.request.UserInfoReq;
import top.yinzsw.blog.service.UserService;
import top.yinzsw.blog.util.CommonUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:14:31
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserManager userManager;
    private final UserConverter userConverter;
    private final UploadProvider uploadProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String updateUserAvatar(MultipartFile avatar) {
        String avatarUrl = uploadProvider.uploadFile(FilePathEnum.AVATAR.getPath(), avatar);
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        userManager.updateById(new UserPO().setId(uid).setAvatar(avatarUrl));
        return avatarUrl;
    }

    @Override
    public boolean updateUserEmail(String email, String code) {
        userManager.checkEmailVerificationCode(email, code);
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        return userManager.updateById(new UserPO().setId(uid).setEmail(email));
    }

    @Override
    public boolean updateUserIsDisable(Long userId, Boolean isDisabled) {
        return userManager.updateById(new UserPO().setId(userId).setIsDisabled(isDisabled));
    }

    @Override
    public boolean updateUserPassword(PasswordByEmailReq password) {
        Optional.ofNullable(userManager.getUserByNameOrEmail(password.getEmail()))
                .orElseThrow(() -> new BizException("邮箱尚未注册"));

        userManager.checkEmailVerificationCode(password.getEmail(), password.getCode());
        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return userManager.updateUserPassword(password.getEmail(), newPassword);
    }

    @Override
    public boolean updateUserPassword(PasswordByOldReq password) {
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        String oldPassword = userManager.lambdaQuery().select(UserPO::getPassword).eq(UserPO::getId, uid).one().getPassword();
        if (!passwordEncoder.matches(password.getOldPassword(), oldPassword)) {
            throw new BizException("旧密码不正确");
        }

        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return userManager.lambdaUpdate().set(UserPO::getPassword, newPassword).eq(UserPO::getId, uid).update();
    }

    @Override
    public boolean updateUserInfo(UserInfoReq userInfoReq) {
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));
        UserPO userPO = userConverter.toUserPO(userInfoReq, uid);
        return userManager.updateById(userPO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserRoles(Long userId, List<Long> roleIds) {
        Db.lambdaUpdate(UserMtmRolePO.class).eq(UserMtmRolePO::getUserId, userId).remove();
        // TODO
        //  尝试主动更新用户token
        //  token 黑名单
        return Db.saveBatch(CommonUtils.toList(roleIds, roleId -> new UserMtmRolePO(userId, roleId)));
    }
}




