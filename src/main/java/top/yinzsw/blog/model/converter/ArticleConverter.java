package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.TagPO;
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

    List<ArticleArchiveVO> toArticleArchiveVO(List<ArticlePO> articlePOList);

    ArticleVO toArticleVO(ArticlePO articlePO, @Context ArticleMapsDTO articleMapsDTO);

    ArticleOutlineVO toArticleOutlineVO(ArticlePO articlePO);

    List<ArticleOutlineVO> toArticleOutlineVO(List<ArticlePO> articlePOList);

    List<ArticlePreviewVO> toArticlePreviewVO(List<ArticlePO> articlePOList, @Context ArticleMapsDTO articleMapsDTO);

    List<ArticleDigestVO> toArticleDigestVO(List<ArticlePO> articlePOList, @Context ArticleMapsDTO articleMapsDTO);

    ArticleBackgroundVO toArticleBackgroundVO(ArticlePO articlePO, @Context ArticleMapsDTO articleMapsDTO);

    List<ArticleDigestBackgroundVO> toArticleDigestBackgroundVO(List<ArticlePO> articlePOList, @Context ArticleMapsDTO articleMapsDTO);

    ArticlePO toArticlePO(ArticleReq articleReq, Long userId, Long categoryId);

    ///////////////////////////////////Context//////////////////////////////////////////////
    List<TagVO> toTagVO(List<TagPO> tagPOList);

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator(ArticlePO origin,
                                 @Context ArticleMapsDTO articleMapsDTO,
                                 @TargetType Class<T> targetType) {
        Long articleId = origin.getId();
        Long categoryId = origin.getCategoryId();

        String categoryName = articleMapsDTO.getCategoryNameMap().get(categoryId);
        List<TagVO> tags = toTagVO(articleMapsDTO.getTagsMap().get(articleId));

        Long likeCount = articleMapsDTO.getLikeCountMap().get(articleId);
        Long viewCount = articleMapsDTO.getViewCountMap().get(articleId);

        if (targetType.isAssignableFrom(ArticleVO.class)) {
            return (T) new ArticleVO().setCategoryName(categoryName).setTags(tags).setViewsCount(viewCount).setLikeCount(likeCount);
        }
        if (targetType.isAssignableFrom(ArticlePreviewVO.class)) {
            return (T) new ArticlePreviewVO().setCategoryName(categoryName).setTags(tags);
        }
        if (targetType.isAssignableFrom(ArticleDigestVO.class)) {
            return (T) new ArticleDigestVO().setCategoryName(categoryName).setTags(tags);
        }
        if (targetType.isAssignableFrom(ArticleBackgroundVO.class)) {
            return (T) new ArticleBackgroundVO().setCategoryName(categoryName).setTags(tags);
        }
        if (targetType.isAssignableFrom(ArticleDigestBackgroundVO.class)) {
            return (T) new ArticleDigestBackgroundVO().setCategoryName(categoryName).setTags(tags).setViewsCount(viewCount).setLikeCount(likeCount);
        }

        throw new UnsupportedOperationException();
    }
}
