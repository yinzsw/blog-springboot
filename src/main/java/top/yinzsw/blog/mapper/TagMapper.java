package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.TagPO;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Mapper
 * @createDate 2023-01-12 22:11:32
 * @Entity top.yinzsw.blog.model.po.TagPO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface TagMapper extends CommonMapper<TagPO> {

    Page<TagPO> pageSearchTags(Page<TagPO> pager, @Param("name") String name);
}




