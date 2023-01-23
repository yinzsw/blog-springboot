package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;

/**
 * @author yinzsW
 * @description 针对表【article_mtm_tag(文章与标签映射表(多对多))】的数据库操作Mapper
 * @createDate 2023-01-13 09:59:04
 * @Entity top.yinzsw.blog.model.po.ArticleMtmTagPO
 */

@CacheNamespace(readWrite = false, blocking = true)
public interface ArticleMtmTagMapper extends BaseMapper<ArticleMtmTagPO> {

}