package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.vo.CategoryDetailVO;
import top.yinzsw.blog.model.vo.CategoryVO;

import java.util.List;
import java.util.Map;

/**
 * 分类数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryConverter {

    CategoryVO toCategoryVO(CategoryPO categoryPO);

    List<CategoryVO> toCategoryVO(List<CategoryPO> records);

    List<CategoryDetailVO> toCategoryDetailVO(List<CategoryPO> categoryPOS, @Context Map<Long, Long> articleCountMap);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    CategoryPO toCategoryPO(CategoryReq categoryReq);

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator(CategoryPO origin,
                                 @Context Map<Long, Long> articleCountMap,
                                 @TargetType Class<T> targetType) {
        Long count = articleCountMap.get(origin.getId());

        if (targetType.isAssignableFrom(CategoryDetailVO.class)) {
            return (T) new CategoryDetailVO().setArticleCount(count);
        }

        throw new UnsupportedOperationException();
    }
}
