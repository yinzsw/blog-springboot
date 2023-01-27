package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.dto.CategoryMapsDTO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.vo.CategoryVO;

import java.util.List;

/**
 * 分类数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryConverter {
    List<CategoryVO> toCategoryVO(List<CategoryPO> categoryPOS, @Context CategoryMapsDTO categoryMapsDTO);

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator(CategoryPO origin,
                                 @Context CategoryMapsDTO categoryMapsDTO,
                                 @TargetType Class<T> targetType) {
        Long tagId = origin.getId();
        Long count = categoryMapsDTO.getMapArticleCount().get(tagId);

        if (targetType.isAssignableFrom(CategoryVO.class)) {
            return (T) new CategoryVO().setArticleCount(count);
        }

        throw new UnsupportedOperationException();
    }
}
