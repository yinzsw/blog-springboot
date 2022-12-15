package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.RolePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Mapper
 * @createDate 2022-12-15 14:47:44
 * @Entity top.yinzsw.blog.model.po.RolePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface RoleMapper extends BaseMapper<RolePO> {

}




