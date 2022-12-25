package top.yinzsw.blog.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yinzsw.blog.enums.LoginTypeEnum;
import top.yinzsw.blog.model.vo.TokenVO;

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
@Schema(description = "用户信息")
public class UserInfoDTO {

    /**
     * 用户账号id
     */
    @Schema(title = "用户账号id")
    private Long id;

    /**
     * 邮箱号
     */
    @Schema(title = "邮箱号")
    private String email;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    private String username;

    /**
     * 用户昵称
     */
    @Schema(title = "用户昵称")
    private String nickname;

    /**
     * 登录方式
     */
    @Schema(title = "登录方式")
    private LoginTypeEnum loginType;

    /**
     * 用户头像
     */
    @Schema(title = "用户头像")
    private String avatar;

    /**
     * 用户简介
     */
    @Schema(title = "用户简介")
    private String intro;

    /**
     * 个人网站
     */
    @Schema(title = "个人网站")
    private String webSite;

    /**
     * 上次登录时的IP地址
     */
    @Schema(title = "上次登录时的IP地址")
    private String ipAddress;

    /**
     * 最近登录时间
     */
    @Schema(title = "最近登录时间")
    private LocalDateTime lastLoginTime;

    /**
     * 点赞文章集合
     */
    @Schema(title = "点赞文章集合")
    private Set<Object> articleLikeSet;

    /**
     * 点赞评论集合
     */
    @Schema(title = "点赞评论集合")
    private Set<Object> commentLikeSet;

    /**
     * 点赞评论集合
     */
    @Schema(title = "点赞评论集合")
    private Set<Object> talkLikeSet;

    /**
     * token信息
     */
    @Schema(title = "令牌信息模型")
    private TokenVO token;
}
