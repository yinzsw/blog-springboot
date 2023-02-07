package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.dto.QueryBackgArticleDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.vo.*;

import java.util.List;

/**
 * 文章数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArticleConverter {

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "prevArticle", ignore = true)
    @Mapping(target = "nextArticle", ignore = true)
    @Mapping(target = "dayHot", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    ArticleVO toArticleVO(ArticlePO articlePO, @Context ArticleMapsDTO articleMapsDTO);

    ArticleOutlineVO toArticleOutlineVO(ArticlePO articlePO);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    ArticleBackgroundVO toArticleBackgroundVO(ArticlePO articlePO, @Context ArticleMapsDTO articleMapsDTO);

    List<ArticleSummaryVO> toArticleSummaryVO(List<ArticlePO> articlePOList);

    List<ArticleDigestVO> toArticleDigestVO(List<ArticlePO> articlePOList, @Context ArticleMapsDTO articleMapsDTO);

    QueryBackgArticleDTO toQueryBackgroundArticleDTO(ArticleQueryReq articleQueryReq, List<Long> articleIds);

    List<ArticleDigestBackgroundVO> toArticleDigestBackgroundVO(List<ArticlePO> articlePOList, @Context ArticleMapsDTO articleMapsDTO);

    List<ArticlePreviewVO> toArticlePreviewVO(List<ArticlePO> articlePOList, @Context ArticleMapsDTO articleMapsDTO);

    List<ArticleArchiveVO> toArticleArchiveVO(List<ArticlePO> articlePOList);

    List<ArticleSearchVO> toArticleSearchVO(List<ArticlePO> articlePOList);


    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "likesCount", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    ArticlePO toArticlePO(ArticleReq articleReq, Long userId);

    ///////////////////////////////////Context//////////////////////////////////////////////

    List<TagVO> toTagVO(List<TagPO> tagPOList);

    ArticleDayHotVO toArticleDayHotVO(ArticleHotIndexDTO hotIndex);

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator(ArticlePO origin,
                                 @Context ArticleMapsDTO articleMapsDTO,
                                 @TargetType Class<T> targetType) {
        Long articleId = origin.getId();
        Long categoryId = origin.getCategoryId();

        String categoryName = articleMapsDTO.getCategoryNameMap().get(categoryId);
        List<TagVO> tags = toTagVO(articleMapsDTO.getTagsMap().get(articleId));
        ArticleDayHotVO articleDayHotVO = toArticleDayHotVO(articleMapsDTO.getHotIndexMap().get(articleId));

        if (targetType.isAssignableFrom(ArticleVO.class)) {
            return (T) new ArticleVO().setCategoryName(categoryName).setTags(tags).setDayHot(articleDayHotVO);
        }
        if (targetType.isAssignableFrom(ArticleBackgroundVO.class)) {
            return (T) new ArticleBackgroundVO().setCategoryName(categoryName).setTags(tags);
        }
        if (targetType.isAssignableFrom(ArticleDigestVO.class)) {
            return (T) new ArticleDigestVO().setCategoryName(categoryName).setTags(tags);
        }
        if (targetType.isAssignableFrom(ArticleDigestBackgroundVO.class)) {
            return (T) new ArticleDigestBackgroundVO().setCategoryName(categoryName).setTags(tags).setDayHot(articleDayHotVO);
        }
        if (targetType.isAssignableFrom(ArticlePreviewVO.class)) {
            return (T) new ArticlePreviewVO().setCategoryName(categoryName).setTags(tags);
        }

        throw new UnsupportedOperationException();
    }
}
