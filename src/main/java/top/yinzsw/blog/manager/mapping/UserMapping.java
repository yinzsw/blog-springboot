package top.yinzsw.blog.manager.mapping;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户映射
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Service
@RequiredArgsConstructor
public class UserMapping {

    public boolean saveRoles(Long userId, List<Long> roleIds) {
        List<UserMtmRolePO> userMtmRolePOList = roleIds.stream()
                .map(roleId -> new UserMtmRolePO(userId, roleId))
                .collect(Collectors.toList());
        return Db.saveBatch(userMtmRolePOList);
    }

    public void deleteRolesMapping(Long userId) {
        Db.lambdaUpdate(UserMtmRolePO.class).eq(UserMtmRolePO::getUserId, userId).remove();
    }
}
