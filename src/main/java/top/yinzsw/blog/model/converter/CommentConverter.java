package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.dto.GroupCommentDTO;
import top.yinzsw.blog.model.po.CommentPO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.vo.CommentVO;
import top.yinzsw.blog.model.vo.MainCommentDetailVO;
import top.yinzsw.blog.model.vo.PageCommentVO;

import java.util.List;
import java.util.Map;

/**
 * 评论数据模型转换器
 *
 * @author yinzsW
 * @since 23/02/13
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentConverter {

    CommentVO toCommentVO(CommentPO commentPO);

    @Mapping(target = "mainComment", expression = "java(mainCommentDetailVO.getComments().poll())", dependsOn = "comments")
    MainCommentDetailVO toMainCommentDetailVO(GroupCommentDTO groupCommentDTO);

    List<MainCommentDetailVO> toMainCommentDetailVO(List<GroupCommentDTO> groupCommentDTOList);

    @Mapping(target = "totalCommentCount", ignore = true)
    @Mapping(target = "mainCommentCount", ignore = true)
    PageCommentVO toPageCommentVO(List<MainCommentDetailVO> mainComments, Map<Long, UserPO> users);
}
