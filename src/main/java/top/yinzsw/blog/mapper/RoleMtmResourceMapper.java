package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author yinzsW
 * @description 针对表【role_mtm_resource(角色与资源映射表(多对多))】的数据库操作Mapper
 * @createDate 2022-12-15 14:49:49
 * @Entity top.yinzsw.blog.model.po.RoleMtmResourcePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface RoleMtmResourceMapper extends BaseMapper<RoleMtmResourcePO> {

}




