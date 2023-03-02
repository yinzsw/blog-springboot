package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.enums.ActionTypeEnum;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.enums.TopicTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.LikeManager;
import top.yinzsw.blog.mapper.LikeMapper;
import top.yinzsw.blog.model.po.LikedPO;

import java.util.Objects;

/**
 * 点赞统一业务处理层实现
 *
 * @author yinzsW
 * @since 23/02/05
 */
@Service
@RequiredArgsConstructor
public class LikeManagerImpl extends ServiceImpl<LikeMapper, LikedPO> implements LikeManager {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void likeStrategy(TopicTypeEnum topicType, Long topicId, boolean like) {
        Long userId = JwtManager
                .getCurrentContextDTO()
                .map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));
        String topicIdParam = topicId.toString();
        String userIdParam = userId.toString();

        String topicName = topicType.getTopicName();
        ActionTypeEnum likeAction = ActionTypeEnum.LIKE;

        String actionUsersKey = RedisConstEnum.TOPIC_ACTION_USERS.getKey(topicName, likeAction.getActionName(), topicId);
        String actionTopicsKey = RedisConstEnum.USER_ACTION_TOPICS.getKey(topicName, likeAction.getActionName(), userId);
        String heatTopicRankingKey = RedisConstEnum.HEAT_TOPIC_RANKING.getKey(topicName);

        SetOperations<String, String> opsForSet = stringRedisTemplate.opsForSet();
        ZSetOperations<String, String> opsForZSet = stringRedisTemplate.opsForZSet();

        LikedPO likedPO = lambdaQuery()
                .select(LikedPO::getId)
                .eq(LikedPO::getUserId, userId)
                .eq(LikedPO::getTopicId, topicId)
                .eq(LikedPO::getTopicType, topicType)
                .one();
        boolean isLikedAgo = Objects.nonNull(likedPO);
        boolean isLikedNow = Boolean.TRUE.equals(opsForSet.isMember(actionTopicsKey, topicIdParam));

        if (like && (isLikedAgo || isLikedNow)) {
            throw new BizException("已经点赞过了, 无法重复点赞");
        }

        if (!like && (!isLikedAgo && !isLikedNow)) {
            throw new BizException("未被点赞, 取消点赞失败");
        }

        if (like && SqlHelper.retBool(opsForSet.add(actionTopicsKey, topicIdParam))) {
            opsForSet.add(actionUsersKey, userIdParam);
            opsForZSet.incrementScore(heatTopicRankingKey, topicIdParam, likeAction.getValue());
            return;
        }

        if (!like && SqlHelper.retBool(opsForSet.remove(actionTopicsKey, topicIdParam))) {
            opsForSet.remove(actionUsersKey, userIdParam);
            opsForZSet.incrementScore(heatTopicRankingKey, topicIdParam, -likeAction.getValue());
            return;
        }

        if (!like && isLikedAgo) {
            removeById(likedPO.getId());
        }
    }
}
