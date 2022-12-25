package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.UserPO;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2022-12-15 14:14:31
 * @Entity top.yinzsw.blog.model.po.UserPO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface UserMapper extends BaseMapper<UserPO> {

}




