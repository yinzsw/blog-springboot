package top.yinzsw.blog.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.constant.RedisConst;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.RedisManager;
import top.yinzsw.blog.model.dto.UserLikedDTO;
import top.yinzsw.blog.util.CommonUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * redis通用业务处理实现
 *
 * @author yinzsW
 * @since 23/01/15
 */
@Service
@RequiredArgsConstructor
public class RedisManagerImpl implements RedisManager, RedisConst {
    private final RedisTemplate<String, ?> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 泛化的值类型 Value Set ZSet List
     *
     * @param <T> redis的值类型
     * @return RedisTemplate
     */
    @SuppressWarnings("unchecked")
    private <T> RedisTemplate<String, T> generalizationRedisTemplate() {
        return (RedisTemplate<String, T>) redisTemplate;
    }

    @Override
    public void saveEmailVerificationCode(String email, String code) {
        String emailCodeKey = createKey(USER_EMAIL_CODE_PREFIX, email);
        stringRedisTemplate.opsForValue().set(emailCodeKey, code, Duration.ofMinutes(USER_EMAIL_CODE_EXPIRE_TIME));
    }

    @Override
    public void checkEmailVerificationCode(String email, String code) throws BizException {
        String emailCodeKey = createKey(USER_EMAIL_CODE_PREFIX, email);
        String emailCode = stringRedisTemplate.opsForValue().get(emailCodeKey);
        if (!code.equalsIgnoreCase(emailCode)) {
            throw new BizException("邮箱验证码错误");
        }
        stringRedisTemplate.delete(emailCodeKey);
    }

    @Override
    public UserLikedDTO getUserLikeInfo(Long userId) {
        SetOperations<String, Long> opsForSet = this.<Long>generalizationRedisTemplate().opsForSet();
        Set<Long> likedTalks = opsForSet.members(createKey(USER_LIKED_TALKS_PREFIX, userId));
        Set<Long> likedArticles = opsForSet.members(createKey(USER_LIKED_ARTICLES_PREFIX, userId));
        Set<Long> likedComments = opsForSet.members(createKey(USER_LIKED_COMMENTS_PREFIX, userId));
        return new UserLikedDTO()
                .setLikedTalkSet(likedTalks)
                .setLikedArticleSet(likedArticles)
                .setLikedCommentSet(likedComments);
    }

    @Override
    public Map<Long, Long> getArticleLikeCount(List<Long> articleIds) {
        List<String> articleIdsKey = articleIds.stream().map(Object::toString).collect(Collectors.toList());
        List<Long> articlesLikedCount = redisTemplate.<String, Long>opsForHash().multiGet(ARTICLE_LIKE_COUNT, articleIdsKey);
        articlesLikedCount = articlesLikedCount.stream()
                .map(count -> Optional.ofNullable(count).orElse(0L))
                .collect(Collectors.toList());
        return CommonUtils.mergeList(articleIds, articlesLikedCount);
    }

    @Override
    public Map<Long, Long> getArticleViewCount(List<Long> articleIds) {
        List<Double> articlesViewScore = redisTemplate.opsForZSet().score(ARTICLE_VIEW_COUNT, articleIds.toArray());
        if (CollectionUtils.isEmpty(articlesViewScore)) {
            return CommonUtils.mergeList(articleIds, Collections.nCopies(articleIds.size(), 0L));
        }

        List<Long> articlesViewCount = articlesViewScore.stream()
                .map(count -> Optional.ofNullable(count).orElse(0D).longValue())
                .collect(Collectors.toList());
        return CommonUtils.mergeList(articleIds, articlesViewCount);
    }
}
