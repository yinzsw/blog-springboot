package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.LikedPO;

@CacheNamespace(readWrite = false, blocking = true)
public interface LikeMapper extends CommonMapper<LikedPO> {
}