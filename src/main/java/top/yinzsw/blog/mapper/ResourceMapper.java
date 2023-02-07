package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.ResourcePO;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Mapper
 * @createDate 2022-12-15 15:00:20
 * @Entity top.yinzsw.blog.model.po.ResourcePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface ResourceMapper extends CommonMapper<ResourcePO> {

}




