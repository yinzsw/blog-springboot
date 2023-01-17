package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.constant.RedisConst;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.RedisManager;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.dto.UserLikedDTO;
import top.yinzsw.blog.model.po.WebsiteConfigPO;
import top.yinzsw.blog.util.MybatisPlusUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * redis通用业务处理实现
 *
 * @author yinzsW
 * @since 23/01/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisManagerImpl implements RedisManager, RedisConst {
    private final RedisTemplate<String, ?> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

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
    public ArticleHotIndexDTO getArticleHotIndex(Long articleId) {
        return getArticleHotIndex(List.of(articleId)).get(articleId);
    }

    @Override
    public Map<Long, ArticleHotIndexDTO> getArticleHotIndex(List<Long> articleIds) {
        Object[] articleIdsKeys = articleIds.stream().map(Object::toString).toArray();
        List<Double> articlesLikeScore = stringRedisTemplate.opsForZSet().score(ARTICLE_LIKE_COUNT, articleIdsKeys);
        List<Double> articlesViewScore = stringRedisTemplate.opsForZSet().score(ARTICLE_VIEW_COUNT, articleIdsKeys);
        return IntStream.range(0, articleIds.size()).boxed().collect(Collectors.toMap(articleIds::get, idx -> {
            ArticleHotIndexDTO articleHotIndexDTO = new ArticleHotIndexDTO();
            Optional.ofNullable(articlesLikeScore).flatMap(likes -> Optional.ofNullable(likes.get(idx)))
                    .map(Double::longValue).ifPresent(articleHotIndexDTO::setLikeCount);
            Optional.ofNullable(articlesViewScore).flatMap(views -> Optional.ofNullable(views.get(idx)))
                    .map(Double::longValue).ifPresent(articleHotIndexDTO::setViewsCount);
            return articleHotIndexDTO;
        }));
    }

    @Override
    public void initWebSiteConfig() {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(WEBSITE_CONFIG))) {
            return;
        }
        Map<String, Object> config = objectMapper.convertValue(new WebsiteConfigPO(), new TypeReference<>() {
        });
        redisTemplate.<String, Object>opsForHash().putAll(WEBSITE_CONFIG, config);
    }

    @Override
    public WebsiteConfigPO getWebSiteConfig() {
        Map<String, Object> websiteConfigMap = redisTemplate.<String, Object>opsForHash().entries(WEBSITE_CONFIG);
        return objectMapper.convertValue(websiteConfigMap, WebsiteConfigPO.class);
    }

    public <T, R> R getWebSiteConfig(SFunction<T, R> sFunction) {
        String propertyName = MybatisPlusUtils.getPropertyName(sFunction);
        return redisTemplate.<String, R>opsForHash().get(WEBSITE_CONFIG, propertyName);
    }
}
