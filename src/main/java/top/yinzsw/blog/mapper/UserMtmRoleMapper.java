package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.UserMtmRolePO;

/**
 * @author yinzsW
 * @description 针对表【user_mtm_role(用户与角色映射表(多对多))】的数据库操作Mapper
 * @createDate 2022-12-15 14:49:49
 * @Entity top.yinzsw.blog.model.po.UserMtmRolePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface UserMtmRoleMapper extends CommonMapper<UserMtmRolePO> {

}




