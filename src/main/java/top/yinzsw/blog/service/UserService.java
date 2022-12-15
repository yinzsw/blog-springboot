package top.yinzsw.blog.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import top.yinzsw.blog.model.po.UserPO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2022-12-15 14:14:31
 */
public interface UserService extends IService<UserPO>, UserDetailsService {

    /**
     * 更新登录信息
     *
     * @param userPO 用户信息
     */
    void updateLoginInfo(UserPO userPO);
}
