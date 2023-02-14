package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.enums.CommentOrderTypeEnum;
import top.yinzsw.blog.enums.TopicTypeEnum;
import top.yinzsw.blog.manager.CommentManager;
import top.yinzsw.blog.mapper.CommentMapper;
import top.yinzsw.blog.model.po.CommentPO;
import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.util.CommonUtils;

import java.util.List;

/**
 * 评论通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/02/13
 */
@Service
public class CommentManagerImpl extends ServiceImpl<CommentMapper, CommentPO> implements CommentManager {
    @Override
    public Page<Long> pageTopicMainCommentIds(Page<CommentPO> pager, CommentQueryReq commentQueryReq) {
        Page<CommentPO> commentPOPage = lambdaQuery()
                .select(CommentPO::getId)
                .eq(CommentPO::getTopicId, commentQueryReq.getTopicId())
                .eq(CommentPO::getTopicType, commentQueryReq.getTopicType())
                .eq(CommentPO::getIsReviewed, true)
                .eq(CommentPO::getLevel, 1)
                .orderByDesc(CommentOrderTypeEnum.DATE.equals(commentQueryReq.getOrderType()), CommentPO::getCreateTime)
                .orderByDesc(CommentOrderTypeEnum.HOT.equals(commentQueryReq.getOrderType()), CommentPO::getLikedCount)
                .page(pager);

        List<Long> commentIds = CommonUtils.toDistinctList(commentPOPage.getRecords(), CommentPO::getId);
        return new Page<Long>().setRecords(commentIds).setTotal(commentPOPage.getTotal());
    }

    @Override
    public long getCommentCount(Long topicId, TopicTypeEnum topicType) {
        return lambdaQuery()
                .eq(CommentPO::getTopicId, topicId)
                .eq(CommentPO::getTopicType, topicType)
                .eq(CommentPO::getIsReviewed, true)
                .count();
    }
}
