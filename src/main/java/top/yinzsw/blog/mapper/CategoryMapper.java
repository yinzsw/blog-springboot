package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.CategoryPO;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Mapper
 * @createDate 2023-01-13 09:57:14
 * @Entity top.yinzsw.blog.model.po.CategoryPO
 */

@CacheNamespace(readWrite = false, blocking = true)
public interface CategoryMapper extends CommonMapper<CategoryPO> {

    /**
     * 分页查询分类名
     *
     * @param pager 分页器
     * @param name  分类名关健词
     * @return 分类分页
     */
    Page<CategoryPO> pageSearchCategories(Page<CategoryPO> pager, @Param("name") String name);
}