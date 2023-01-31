package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 查看后台留言
 *
 * @author yinzsW
 * @since 23/01/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "留言(后台)")
public class MessageBackgroundVO {

    /**
     * 留言id
     */
    @Schema(title = "留言id")
    private Long id;

    /**
     * 用户id
     */
    @Schema(title = "用户id")
    private Long userId;

    /**
     * 用户ip
     */
    @Schema(title = "用户ip")
    private String ipAddress;

    /**
     * 用户ip归属地址
     */
    @Schema(title = "用户ip归属地址")
    private String ipSource;

    /**
     * 用户昵称
     */
    @Schema(title = "用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @Schema(title = "用户头像")
    private String avatar;

    /**
     * 留言内容
     */
    @Schema(title = "留言内容")
    private String messageContent;

    /**
     * 是否审核
     */
    @Schema(title = "是否审核")
    private Boolean isReview;

    /**
     * 留言时间
     */
    @Schema(title = "留言时间")
    private LocalDateTime createTime;
}
