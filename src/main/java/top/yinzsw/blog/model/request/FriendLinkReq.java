package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 友链
 *
 * @author yinzsW
 * @since 23/01/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "友链")
public class FriendLinkReq {

    /**
     * 友链id
     */
    @Schema(title = "友链id")
    private Long id;

    /**
     * 链接名
     */
    @NotBlank(message = "链接名不能为空")
    @Schema(title = "链接名")
    private String linkName;

    /**
     * 链接头像
     */
    @NotBlank(message = "头像链接不能为空")
    @Schema(title = "友链头像链接")
    private String linkAvatar;

    /**
     * 链接地址
     */
    @NotBlank(message = "友链链接地址不能为空")
    @Pattern(regexp = "https?://\\S*", message = "友链地址不合法", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Schema(title = "友链链接地址")
    private String linkAddress;

    /**
     * 友链信息介绍
     */
    @NotBlank(message = "友链介绍不能为空")
    @Schema(title = "友链介绍")
    private String linkIntro;
}
