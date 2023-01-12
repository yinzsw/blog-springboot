package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.TagPO;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Mapper
 * @createDate 2023-01-12 22:11:32
 * @Entity top.yinzsw.blog.model.po.TagPO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface TagMapper extends BaseMapper<TagPO> {

}




