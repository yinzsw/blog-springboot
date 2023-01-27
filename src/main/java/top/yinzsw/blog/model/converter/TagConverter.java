package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.dto.TagMapsDTO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.vo.TagSearchVO;
import top.yinzsw.blog.model.vo.TagVO;

import java.util.List;

/**
 * 标签数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagConverter {
    List<TagVO> toTagVO(List<TagPO> tagPOList);

    List<TagSearchVO> toTagSearchVO(List<TagPO> tagPOS, @Context TagMapsDTO tagMapsDTO);

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator(TagPO origin,
                                 @Context TagMapsDTO tagMapsDTO,
                                 @TargetType Class<T> targetType) {
        Long tagId = origin.getId();
        Long count = tagMapsDTO.getMapArticleCount().get(tagId);

        if (targetType.isAssignableFrom(TagSearchVO.class)) {
            return (T) new TagSearchVO().setArticleCount(count);
        }

        throw new UnsupportedOperationException();
    }
}
