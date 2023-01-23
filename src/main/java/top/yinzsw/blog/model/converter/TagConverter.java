package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.TagPO;
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
}
