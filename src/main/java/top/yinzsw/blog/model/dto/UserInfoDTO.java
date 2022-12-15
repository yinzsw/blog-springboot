package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yinzsw.blog.enums.LoginTypeEnum;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户信息
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    /**
     * 用户账号id
     */
    private Long id;

    /**
     * 邮箱号
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 登录方式
     */
    private LoginTypeEnum loginType;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String intro;

    /**
     * 个人网站
     */
    private String webSite;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 点赞文章集合
     */
    private Set<Object> articleLikeSet;

    /**
     * 点赞评论集合
     */
    private Set<Object> commentLikeSet;

    /**
     * 点赞评论集合
     */
    private Set<Object> talkLikeSet;
}
