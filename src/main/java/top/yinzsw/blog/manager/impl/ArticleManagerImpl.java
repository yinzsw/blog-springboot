package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.task.RunnableTaskHandler;
import top.yinzsw.blog.enums.ActionTypeEnum;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.enums.TopicTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.MapQueryUtils;

import java.util.*;
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
public class ArticleManagerImpl extends ServiceImpl<ArticleMapper, ArticlePO> implements ArticleManager {
    private final RunnableTaskHandler runnableTaskHandler;
    private final StringRedisTemplate stringRedisTemplate;

    @Async
    @Override
    public void updateViewsInfo(Long articleId, String userIdentify) {
        String articleIdParam = Optional.ofNullable(articleId).map(Object::toString)
                .orElseThrow(BizException::new);

        String topicName = TopicTypeEnum.ARTICLE.getTopicName();
        String actionName = ActionTypeEnum.VIEW.getActionName();
        String actionUsersKey = RedisConstEnum.TOPIC_ACTION_USERS.getKey(topicName, actionName, articleId);
        String actionTopicsKey = RedisConstEnum.USER_ACTION_TOPICS.getKey(topicName, actionName, userIdentify);
        String topicRankingKey = RedisConstEnum.HEAT_TOPIC_RANKING.getKey(actionName);

        SetOperations<String, String> opsForSet = stringRedisTemplate.opsForSet();
        if (Long.valueOf(1L).equals(opsForSet.add(actionTopicsKey, articleIdParam))) {
            opsForSet.add(actionUsersKey, userIdentify);
            stringRedisTemplate.opsForZSet().incrementScore(topicRankingKey, articleIdParam, ActionTypeEnum.VIEW.getValue());
        }
    }

    ////////////////////////////////////////////////MYSQL///////////////////////////////////////////////////////////////
    @Override
    public Page<ArticlePO> pageArchivesArticles(Page<ArticlePO> pager) {
        return getVisibleArticleWrapper()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getArticleStatus, ArticlePO::getCreateTime)
                .orderByDesc(ArticlePO::getCreateTime)
                .page(pager);
    }

    @Override
    public ArticlePO getArticleById(Long articleId) {
        return getVisibleArticleWrapper()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleContent, ArticlePO::getArticleCover, ArticlePO::getLikesCount,
                        ArticlePO::getViewsCount, ArticlePO::getArticleStatus, ArticlePO::getArticleType,
                        ArticlePO::getOriginalUrl, ArticlePO::getCreateTime, ArticlePO::getUpdateTime)
                .eq(ArticlePO::getId, articleId).one();
    }

    @Override
    public Page<ArticlePO> pagePreviewArticlesByCategoryId(Page<ArticlePO> pager, Long categoryId) {
        return getVisibleArticleWrapper()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleCover,
                        ArticlePO::getArticleTitle, ArticlePO::getArticleStatus, ArticlePO::getCreateTime)
                .eq(ArticlePO::getCategoryId, categoryId)
                .page(pager);
    }

    @Override
    public Page<ArticlePO> pagePreviewArticlesByIds(Page<ArticlePO> pager, List<Long> articleIds) {
        return getVisibleArticleWrapper()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleCover,
                        ArticlePO::getArticleTitle, ArticlePO::getArticleStatus, ArticlePO::getCreateTime)
                .in(ArticlePO::getId, articleIds)
                .page(pager);
    }

    @Override
    public ArticleMapsDTO getArticleMapsDTO(List<ArticlePO> articlePOList, boolean mapHotIndex) {
        if (CollectionUtils.isEmpty(articlePOList)) {
            return new ArticleMapsDTO();
        }

        List<Long> articleIds = CommonUtils.toDistinctList(articlePOList, ArticlePO::getId);
        List<Long> categoryIds = CommonUtils.toDistinctList(articlePOList, ArticlePO::getCategoryId);

        ArticleMapsDTO mapsDTO = new ArticleMapsDTO();

        // 设置分类名Map
        Runnable setCategoryNameMapTask = () -> {
            Map<Long, String> categoryNameMap = MapQueryUtils.create(CategoryPO::getId, categoryIds)
                    .getKeyValueMap(CategoryPO::getCategoryName);
            mapsDTO.setCategoryNameMap(categoryNameMap);
        };

        // 设置标签Map
        Runnable setTagsMapTask = () -> {
            Map<Long, List<Long>> tagIdsMap = MapQueryUtils.create(ArticleMtmTagPO::getArticleId, articleIds)
                    .getGroupValueMap(ArticleMtmTagPO::getTagId);

            List<Long> distinctTagIds = tagIdsMap.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
            Map<Long, TagPO> tagPOMap = MapQueryUtils.create(TagPO::getId, distinctTagIds)
                    .queryWrapper(q -> q.select(TagPO::getId, TagPO::getTagName))
                    .getKeyMap();

            Map<Long, List<TagPO>> tagsMap = tagIdsMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                    entryValue -> CommonUtils.toList(entryValue.getValue(), tagPOMap::get)));
            mapsDTO.setTagsMap(tagsMap);
        };

        // 设置热度Map
        Runnable setHotIndexMapTask = () -> {
            String topicName = TopicTypeEnum.ARTICLE.getTopicName();
            String likeActionName = ActionTypeEnum.LIKE.getActionName();
            String viewActionName = ActionTypeEnum.VIEW.getActionName();

            List<Object> results = stringRedisTemplate.executePipelined(CommonUtils.<String, String>getSessionCallback(operations -> {
                SetOperations<String, String> opsForSet = operations.opsForSet();
                articleIds.stream().map(Object::toString).forEach(articleId -> {
                    opsForSet.size(RedisConstEnum.TOPIC_ACTION_USERS.getKey(topicName, likeActionName, articleId));
                    opsForSet.size(RedisConstEnum.TOPIC_ACTION_USERS.getKey(topicName, viewActionName, articleId));
                });
            }));

            Map</*文章id*/Long, ArticleHotIndexDTO> HotIndexMap = IntStream.range(0, articleIds.size()).boxed()
                    .collect(Collectors.toMap(articleIds::get, i -> {
                        Long likesCount = (Long) results.get(i * 2);
                        Long viewsCount = (Long) results.get(i * 2 + 1);
                        return new ArticleHotIndexDTO().setLikesCount(likesCount).setViewsCount(viewsCount);
                    }));

            mapsDTO.setHotIndexMap(HotIndexMap);
        };

        runnableTaskHandler.handler()
                .addTask(setCategoryNameMapTask)
                .addTask(setTagsMapTask)
                .addTask(mapHotIndex, setHotIndexMapTask)
                .handle();
        return mapsDTO;
    }

    private LambdaQueryChainWrapper<ArticlePO> getVisibleArticleWrapper() {
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid).orElse(null);
        return lambdaQuery().and(w -> w.eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .or(Objects.nonNull(uid), wr -> wr
                        .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.SECRET)
                        .eq(ArticlePO::getUserId, uid)));
    }
}
