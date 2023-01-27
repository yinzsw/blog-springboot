package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 友链
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "友链")
public class FriendLinkVO {

    /**
     * 友链id
     */
    @Schema(title = "友链id")
    private Long id;

    /**
     * 链接名
     */
    @Schema(title = "链接名")
    private String linkName;

    /**
     * 链接头像
     */
    @Schema(title = "链接头像")
    private String linkAvatar;

    /**
     * 链接地址
     */
    @Schema(title = "链接地址")
    private String linkAddress;

    /**
     * 链接介绍
     */
    @Schema(title = "链接介绍")
    private String linkIntro;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
