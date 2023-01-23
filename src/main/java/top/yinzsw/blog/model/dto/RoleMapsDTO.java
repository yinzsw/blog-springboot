package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 角色映射信息模型
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleMapsDTO {

    /**
     * 角色id->菜单id列表
     */
    private Map<Long, List<Long>> menuIdsMap = Collections.emptyMap();

    /**
     * 角色id->资源id列表
     */
    private Map<Long, List<Long>> resourceIdsMap = Collections.emptyMap();
}
