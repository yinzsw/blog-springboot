package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.RolePO;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Mapper
 * @createDate 2022-12-15 14:47:44
 * @Entity top.yinzsw.blog.model.po.RolePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface RoleMapper extends CommonMapper<RolePO> {

    /**
     * 分页角色
     *
     * @param pager    分页器
     * @param keywords 关键词
     * @return 角色 分页
     */
    Page<RolePO> pageSearchRoles(Page<RolePO> pager, @Param("keywords") String keywords);
}




