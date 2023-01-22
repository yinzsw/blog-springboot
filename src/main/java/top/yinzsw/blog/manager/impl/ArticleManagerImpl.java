package top.yinzsw.blog.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        List<Double> articlesLikeScore = stringRedisTemplate.opsForZSet().score(ArticleManager.ARTICLE_LIKE_COUNT, (Object[]) articleIds);
        return getMap(articlesLikeScore, articleIds);
    }

    @Override
    public Map<Long, Long> getViewsCountMap(Long... articleIds) {
        List<Double> articlesViewScore = stringRedisTemplate.opsForZSet().score(ArticleManager.ARTICLE_VIEW_COUNT, (Object[]) articleIds);
        return getMap(articlesViewScore, articleIds);
    }

    @Override
    public void updateViewsCount(Long articleId) {
        //todo 更新文章浏览量
        try {
            //登录用户处理
            httpContext.getCurrentContextDTO();
        } catch (BizException e) {
            //游客处理
            throw new RuntimeException(e);
        }
    }

    private static Map<Long, Long> getMap(List<Double> articlesScore, Long[] articleIds) {
        return IntStream.range(0, articleIds.length).boxed().collect(Collectors.toMap(idx -> articleIds[idx], idx -> Optional
                .ofNullable(articlesScore)
                .flatMap(scores -> Optional.ofNullable(scores.get(idx)))
                .map(Double::longValue).orElse(0L)));
    }
}
