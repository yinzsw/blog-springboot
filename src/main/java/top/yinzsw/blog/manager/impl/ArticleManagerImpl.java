package top.yinzsw.blog.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 文章数据映射模型
 *
 * @author yinzsW
 * @since 23/01/18
 */
@Service
@RequiredArgsConstructor
public class ArticleManagerImpl implements ArticleManager {
    private final StringRedisTemplate stringRedisTemplate;
    private final HttpContext httpContext;

    @Override
    public Map<Long, Long> getLikesCountMap(Long... articleIds) {
        return getScoreMapByKey(ARTICLE_LIKE_COUNT, articleIds);
    }

    @Override
    public Map<Long, Long> getViewsCountMap(Long... articleIds) {
        return getScoreMapByKey(ARTICLE_VIEW_COUNT, articleIds);
    }

    @Override
    public void updateLikeCount(Long articleId, Long delta) {
        stringRedisTemplate.opsForZSet().incrementScore(ARTICLE_LIKE_COUNT, String.valueOf(articleId), delta);
    }

    @Override
    public void updateViewsCount(Long articleId) {
        Supplier<String> suffixSupplier = () -> {
            try {
                return httpContext.getCurrentContextDTO().getUid().toString();
            } catch (BizException e) {
                return httpContext.getUserIpAddress();
            }
        };
        String antiKey = ARTICLE_VIEW_ANTI_PREFIX + suffixSupplier.get();
        Boolean hasKey = stringRedisTemplate.hasKey(antiKey);
        if (Boolean.FALSE.equals(hasKey)) {
            stringRedisTemplate.opsForValue().set(antiKey, "", Duration.ofHours(1));
            stringRedisTemplate.opsForZSet().incrementScore(ARTICLE_VIEW_COUNT, String.valueOf(articleId), 1L);
        }
    }

    private Map<Long, Long> getScoreMapByKey(String key, Long[] articleIds) {
        Object[] ids = Arrays.stream(articleIds).map(Object::toString).toArray();
        List<Double> score = stringRedisTemplate.opsForZSet().score(key, ids);
        return IntStream.range(0, articleIds.length).boxed().collect(Collectors.toMap(idx -> articleIds[idx], idx -> Optional
                .ofNullable(score)
                .flatMap(scores -> Optional.ofNullable(scores.get(idx)))
                .map(Double::longValue).orElse(0L)));
    }
}
