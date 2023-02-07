package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 邮箱验证码传输模型
 *
 * @author yinzsW
 * @since 23/01/01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmailCodeDTO {
    /**
     * 接收者邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 有效期分钟数
     */
    private long timeoutMinutes;
}
