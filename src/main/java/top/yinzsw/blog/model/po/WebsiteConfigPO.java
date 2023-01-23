package top.yinzsw.blog.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 网站配置
 *
 * @author yinzsW
 * @since 23/01/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WebsiteConfigPO {

    /**
     * QQ
     */
    private String qq = "";

    /**
     * Github
     */
    private String github = "";

    /**
     * Gitee
     */
    private String gitee = "";

    /**
     * 是否开启打赏
     */
    private Boolean isReward = false;

    /**
     * 是否开启聊天室
     */
    private Boolean isChatRoom = false;

    /**
     * 是否开启邮箱通知
     */
    private Boolean isEmailNotice = false;

    /**
     * 是否开启音乐
     */
    private Boolean isMusicPlayer = false;

    /**
     * 是否评论审核
     */
    private Boolean isCommentReview = false;

    /**
     * 是否留言审核
     */
    private Boolean isMessageReview = false;

    /**
     * 网站名称
     */
    private String websiteName = "个人博客";

    /**
     * 网站头像
     */
    private String websiteAvatar = "https://static.talkxj.com/photos/b553f564f81a80dc338695acb1b475d2.jpg";

    /**
     * 网站作者
     */
    private String websiteAuthor = "";

    /**
     * 网站介绍
     */
    private String websiteIntro = "";

    /**
     * 网站公告
     */
    private String websiteNotice = "";

    /**
     * 网站创建时间
     */
    private String websiteCreateTime = "";

    /**
     * 网站备案号
     */
    private String websiteRecordNo = "";

    /**
     * 用户头像
     */
    private String userAvatar = "https://static.talkxj.com/config/2cd793c8744199053323546875655f32.jpg";

    /**
     * 游客头像
     */
    private String touristAvatar = "https://static.talkxj.com/photos/0bca52afdb2b9998132355d716390c9f.png";

    /**
     * 文章封面
     */
    private String articleCover = "https://static.talkxj.com/config/e587c4651154e4da49b5a54f865baaed.jpg";

    /**
     * 微信二维码
     */
    private String weiXinQRCode = "https://static.talkxj.com/photos/4f767ef84e55ab9ad42b2d20e51deca1.png";

    /**
     * 支付宝二维码
     */
    private String alipayQRCode = "https://static.talkxj.com/photos/13d83d77cc1f7e4e0437d7feaf56879f.png";

    /**
     * websocket地址
     */
    private String websocketUrl = "";

    /**
     * 社交登录列表
     */
    private List<String> socialLoginList = List.of("qq", "weibo");

    /**
     * 社交url列表
     */
    private List<String> socialUrlList = List.of("qq", "github", "gitee");
}
