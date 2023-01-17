package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.dto.ArticleMappingDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.vo.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    ArticleDigestHomeVO toArticleDigestHomeVO(ArticlePO articlePO, String categoryName, List<TagPO> tagList);

    default List<ArticleDigestHomeVO> toArticleDigestHomeVO(List<ArticlePO> articlePOList,
                                                            Map<Long, String> categoryMapping,
                                                            Map<Long, List<TagPO>> tagMapping) {
        return articlePOList.stream().map(articlePO -> {
            Optional.ofNullable(articlePO.getArticleContent()).ifPresent(articleContent -> {
                int endIndex = Math.min(articleContent.length(), 512);
                articlePO.setArticleContent(articleContent.substring(0, endIndex));
            });
            String categoryName = categoryMapping.get(articlePO.getCategoryId());
            List<TagPO> tagPOList = tagMapping.get(articlePO.getId());

            return toArticleDigestHomeVO(articlePO, categoryName, tagPOList);
        }).collect(Collectors.toList());
    }

    ArticleDigestBackVO toArticleDigestBackVO(ArticlePO articlePO, String categoryName, List<TagPO> tagList, Long likeCount, Long viewsCount);

    default List<ArticleDigestBackVO> toArticleDigestBackVO(List<ArticlePO> articlePOList, ArticleMappingDTO articleMappingDTO) {
        return articlePOList.stream().map(articlePO -> {
            String categoryName = articleMappingDTO.getCategoryNameMapping().get(articlePO.getCategoryId());
            Long articleId = articlePO.getId();
            List<TagPO> tagPOList = articleMappingDTO.getTagMapping().get(articleId);

            ArticleHotIndexDTO articleHotIndexDTO = articleMappingDTO.getArticleHotIndexMapping().get(articleId);
            Long likeCount = articleHotIndexDTO.getLikeCount();
            Long viewsCount = articleHotIndexDTO.getViewsCount();
            return toArticleDigestBackVO(articlePO, categoryName, tagPOList, likeCount, viewsCount);
        }).collect(Collectors.toList());
    }


    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    ArticlePO toArticlePO(ArticleReq articleReq, Long userId, Long categoryId);

    ArticleBackVO toArticleBackVO(ArticlePO articlePO, String categoryName, List<TagPO> tags);

    ArticleOutlineHomeVO toArticleOutlineHomeVO(ArticlePO articlePO);

    List<ArticleOutlineHomeVO> toArticleOutlineHomeVO(List<ArticlePO> articlePOList);


    @Mapping(target = "relatedRecommendArticles", ignore = true)
    @Mapping(target = "prevArticle", ignore = true)
    @Mapping(target = "nextArticle", ignore = true)
    @Mapping(target = "newestRecommendArticles", ignore = true)
    ArticleHomeVO toArticleHomeVO(ArticlePO articlePO, String categoryName, List<TagPO> tags, ArticleHotIndexDTO articleHotIndexDTO);
}
