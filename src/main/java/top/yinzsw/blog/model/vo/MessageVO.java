package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 留言
 *
 * @author yinzsW
 * @since 23/01/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "留言")
public class MessageVO {

    /**
     * 留言id
     */
    @Schema(title = "留言id")
    private Long id;

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
}
