package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;

import java.util.List;

/**
 * 文章数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArticleConverter {
    List<ArticleArchiveVO> toArticleArchiveVO(List<ArticlePO> articlePOList);
}
