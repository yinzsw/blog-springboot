package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.model.po.ArticlePO;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Mapper
 * @createDate 2023-01-12 23:17:07
 * @Entity top.yinzsw.blog.model.po.ArticlePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface ArticleMapper extends BaseMapper<ArticlePO> {

}




