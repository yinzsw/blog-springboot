package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.dto.ArticleMappingDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;
import top.yinzsw.blog.model.vo.ArticleBackVO;
import top.yinzsw.blog.model.vo.ArticleHomeVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArticleConverter {
    List<ArticleArchiveVO> toArticleArchiveVO(List<ArticlePO> articlePOList);

    ArticleHomeVO toArticleHomeVO(ArticlePO articlePO, String categoryName, List<TagPO> tagList);

    default List<ArticleHomeVO> toArticleHomeVO(List<ArticlePO> articlePOList,
                                                Map<Long, String> categoryMapping,
                                                Map<Long, List<TagPO>> tagMapping) {
        return articlePOList.stream().map(articlePO -> {
            Long articleId = articlePO.getId();
            Long categoryId = articlePO.getCategoryId();
            return toArticleHomeVO(articlePO, categoryMapping.get(categoryId), tagMapping.get(articleId));
        }).collect(Collectors.toList());
    }

    ArticleBackVO toArticleBackVO(ArticlePO articlePO, String categoryName, List<TagPO> tagList, Long likeCount, Long viewsCount);

    default List<ArticleBackVO> toArticleBackVO(List<ArticlePO> articlePOList, ArticleMappingDTO articleMappingDTO) {
        return articlePOList.stream().map(articlePO -> {
            Long articleId = articlePO.getId();
            Long categoryId = articlePO.getCategoryId();
            return toArticleBackVO(articlePO,
                    articleMappingDTO.getCategoryNameMapping().get(categoryId),
                    articleMappingDTO.getTagMapping().get(articleId),
                    articleMappingDTO.getLikeCountMapping().get(articleId),
                    articleMappingDTO.getViewCountMapping().get(articleId));
        }).collect(Collectors.toList());
    }


    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    ArticlePO toArticlePO(ArticleReq articleReq, Long userId, Long categoryId);
}
