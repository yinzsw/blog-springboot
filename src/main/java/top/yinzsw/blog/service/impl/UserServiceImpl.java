package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.extension.context.HttpContext;
import top.yinzsw.blog.extension.upload.UploadStrategy;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.UserInfoRequest;
import top.yinzsw.blog.service.UserService;

import java.util.regex.Pattern;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:14:31
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {
    private static final Pattern EMAIL_REGEXP = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    private final HttpContext httpContext;
    private final UploadStrategy uploadStrategy;
    private final UserConverter userConverter;

    @Override
    public UserPO getUserByNameOrEmail(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        boolean isEmail = EMAIL_REGEXP.matcher(keyword).find();
        return getOne(new LambdaQueryWrapper<UserPO>()
                .select(UserPO::getId, UserPO::getUsername, UserPO::getPassword,
                        UserPO::getEmail, UserPO::getNickname, UserPO::getAvatar,
                        UserPO::getIntro, UserPO::getWebSite, UserPO::getIsDisabled)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, keyword));
    }

    @Override
    public Boolean updateUserInfo(UserInfoRequest userInfoRequest) {
        Long uid = httpContext.getCurrentClaimsDTO().getUid();
        UserPO userPO = userConverter.toUserPO(userInfoRequest, uid);
        return updateById(userPO);
    }

    @Override
    public String updateUserAvatar(MultipartFile file) {
        String avatarUrl = uploadStrategy.uploadFile(FilePathEnum.AVATAR.getPath(), file);
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        UserPO userPO = new UserPO();
        userPO.setId(uid);
        userPO.setAvatar(avatarUrl);
        updateById(userPO);
        return avatarUrl;
    }
}




