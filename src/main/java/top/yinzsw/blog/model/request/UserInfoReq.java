package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 用户信息
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户信息")
public class UserInfoReq {

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Schema(title = "昵称")
    private String nickname;

    /**
     * 用户简介
     */
    @Schema(title = "介绍")
    private String intro;

    /**
     * 个人网站
     */
    @Schema(title = "个人网站")
    private String webSite;
}
