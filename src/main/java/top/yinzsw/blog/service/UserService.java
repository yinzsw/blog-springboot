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
     * 格局用户名或邮箱查询用户
     *
     * @param identity 用户身份标识字符串
     * @return 用户
     */
    UserPO getUserByNameOrEmail(String identity);

    /**
     * 更新用户信息
     *
     * @param userInfoReq 用户信息
     * @return 是否更新成功
     */
    Boolean updateUserInfo(UserInfoReq userInfoReq);

    /**
     * 更新用户头像
     *
     * @param file 用户头像文件
     * @return 头像地址
     */
    String updateUserAvatar(MultipartFile file);

    /**
     * 更新或绑定用户邮箱
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 更新状态
     */
    Boolean updateUserEmail(String email, String code);

    /**
     * 更新用户禁用状态
     *
     * @param userId  用户id
     * @param disable 禁用状态
     * @return 更新状态
     */
    Boolean updateUserDisable(Long userId, Boolean disable);

    /**
     * 根据用户名或邮箱修改密码
     *
     * @param identity    用户身份标识字符串
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean updateUserPassword(String identity, String newPassword);

    /**
     * 根据邮箱更新用户密码
     *
     * @param password 密码验证信息
     * @return 是否成功
     */
    Boolean updateUserPassword(PasswordByEmailReq password);

    /**
     * 根据旧密码更新用户密码
     *
     * @param password 密码验证信息
     * @return 是否成功
     */
    Boolean updateUserPassword(PasswordByOldReq password);
}
