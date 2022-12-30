package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.UserInfoRequest;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2022-12-15 14:14:31
 */
public interface UserService extends IService<UserPO> {

    /**
     * 格局用户名或邮箱查询用户
     *
     * @param keyword 关键词
     * @return 用户
     */
    UserPO getUserByNameOrEmail(String keyword);

    /**
     * 更新用户信息
     *
     * @param userInfoRequest 用户信息
     * @return 是否更新成功
     */
    Boolean updateUserInfo(UserInfoRequest userInfoRequest);

    /**
     * 更新用户头像
     *
     * @param file 用户头像文件
     * @return 头像地址
     */
    String updateUserAvatar(MultipartFile file);
}
