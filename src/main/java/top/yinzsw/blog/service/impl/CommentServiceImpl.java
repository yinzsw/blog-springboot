package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.CommentManager;
import top.yinzsw.blog.mapper.CommentMapper;
import top.yinzsw.blog.model.converter.CommentConverter;
import top.yinzsw.blog.model.dto.GroupCommentDTO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.MainCommentDetailVO;
import top.yinzsw.blog.model.vo.PageCommentVO;
import top.yinzsw.blog.service.CommentService;
import top.yinzsw.blog.util.MapQueryUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 评论业务接口实现
 *
 * @author yinzsW
 * @since 23/02/13
 */

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentManager commentManager;
    private final CommentConverter commentConverter;

    @Override
    public PageCommentVO pageTopicComments(PageReq pageReq, CommentQueryReq commentQueryReq) {
        Page<Long> topicMainCommentIds = commentManager.pageTopicMainCommentIds(pageReq.getPager(), commentQueryReq);

        VerifyUtils.checkIPage(topicMainCommentIds);

        List<GroupCommentDTO> groupCommentDTOList =
                commentMapper.getGroupCommentDTO(topicMainCommentIds.getRecords(), commentQueryReq.getOrderType());
        List<MainCommentDetailVO> mainCommentDetailVOList = commentConverter.toMainCommentDetailVO(groupCommentDTOList);

        //查询参与评论的用户
        List<Long> userIds = groupCommentDTOList.stream()
                .flatMap(groupCommentDTO -> groupCommentDTO.getComments().stream()
                        .flatMap(commentPO -> Stream.of(commentPO.getUserId(), commentPO.getReplyUserId())))
                .distinct()
                .collect(Collectors.toList());
        Map<Long, UserPO> userPOMap = MapQueryUtils.create(UserPO::getId, userIds)
                .queryWrapper(q -> q.select(UserPO::getId, UserPO::getNickname, UserPO::getAvatar, UserPO::getWebSite))
                .getKeyMap();

        long totalCommentCount = commentManager.getCommentCount(commentQueryReq.getTopicId(), commentQueryReq.getTopicType());
        return commentConverter.toPageCommentVO(mainCommentDetailVOList, userPOMap)
                .setMainCommentCount(topicMainCommentIds.getTotal())
                .setTotalCommentCount(totalCommentCount);
    }
}
