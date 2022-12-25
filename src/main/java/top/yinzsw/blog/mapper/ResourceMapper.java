package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.ResourcePO;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Mapper
 * @createDate 2022-12-15 15:00:20
 * @Entity top.yinzsw.blog.model.po.ResourcePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface ResourceMapper extends BaseMapper<ResourcePO> {

}




