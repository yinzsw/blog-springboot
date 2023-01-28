package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.FriendLinkPO;

/**
 * @author yinzsW
 * @description 针对表【friend_link(友链表)】的数据库操作Mapper
 * @createDate 2023-01-27 23:08:47
 * @Entity top.yinzsw.blog.model.po.FriendLinkPO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface FriendLinkMapper extends BaseMapper<FriendLinkPO> {

}




