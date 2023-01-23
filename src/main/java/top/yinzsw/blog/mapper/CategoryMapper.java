package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.CategoryPO;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Mapper
 * @createDate 2023-01-13 09:57:14
 * @Entity top.yinzsw.blog.model.po.CategoryPO
 */

@CacheNamespace(readWrite = false, blocking = true)
public interface CategoryMapper extends BaseMapper<CategoryPO> {

}