package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.MessagePO;

/**
 * @author yinzsW
 * @description 针对表【message(留言表)】的数据库操作Mapper
 * @createDate 2023-01-28 20:21:06
 * @Entity top.yinzsw.blog.model.po.MessagePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface MessageMapper extends BaseMapper<MessagePO> {

}




