package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;

/**
 * @author yinzsW
 * @description 针对表【role_mtm_menu(角色与菜单映射表(多对多))】的数据库操作Mapper
 * @createDate 2023-01-02 18:05:54
 * @Entity top.yinzsw.blog.model.po.RoleMtmMenuPO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface RoleMtmMenuMapper extends CommonMapper<RoleMtmMenuPO> {

}




