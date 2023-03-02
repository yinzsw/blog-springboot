package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.enums.CommentOrderTypeEnum;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.dto.GroupCommentDTO;
import top.yinzsw.blog.model.po.CommentPO;

import java.util.List;

@CacheNamespace(readWrite = false, blocking = true)
public interface CommentMapper extends CommonMapper<CommentPO> {

    /**
     * 获取评论列表映射
     *
     * @param commentIds 评论id
     * @param orderType  排序类型
     * @return 评论列表映射
     */
    List<GroupCommentDTO> getGroupCommentDTO(@Param("commentIds") List<Long> commentIds,
                                             @Param("orderType") CommentOrderTypeEnum orderType);
}