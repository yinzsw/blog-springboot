package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.PasswordByEmailReq;
import top.yinzsw.blog.model.request.PasswordByOldReq;
import top.yinzsw.blog.model.request.UserInfoReq;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2022-12-15 14:14:31
 */
public interface UserService extends IService<UserPO> {

    /**
     * 更新用户信息
     *
     * @param userInfoReq 用户信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(UserInfoReq userInfoReq);

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像文件
     * @return 头像地址
     */
    String updateUserAvatar(MultipartFile avatar);

    /**
     * 更新或绑定用户邮箱
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 更新状态
     */
    boolean updateUserEmail(String email, String code);

    /**
     * 更新用户禁用状态
     *
     * @param userId     用户id
     * @param isDisabled 禁用状态
     * @return 更新状态
     */
    boolean updateUserIsDisable(Long userId, Boolean isDisabled);

    /**
     * 根据邮箱更新用户密码
     *
     * @param password 密码验证信息
     * @return 是否成功
     */
    boolean updateUserPassword(PasswordByEmailReq password);

    /**
     * 根据旧密码更新用户密码
     *
     * @param password 密码验证信息
     * @return 是否成功
     */
    boolean updateUserPassword(PasswordByOldReq password);
}
