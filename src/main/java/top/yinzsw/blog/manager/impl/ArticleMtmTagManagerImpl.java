package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.extension.mybatisplus.service.MappingServiceImpl;
import top.yinzsw.blog.manager.ArticleMtmTagManager;
import top.yinzsw.blog.mapper.ArticleMtmTagMapper;
import top.yinzsw.blog.mapper.TagMapper;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.util.CommonUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【article_mtm_tag(文章与标签映射表(多对多))】的数据库操作Service实现
 * @createDate 2023-01-13 09:59:04
 */
@Service
@RequiredArgsConstructor
public class ArticleMtmTagManagerImpl extends MappingServiceImpl<ArticleMtmTagMapper, ArticleMtmTagPO> implements ArticleMtmTagManager {
    private final TagMapper tagMapper;

    @Async
    @Override
    public CompletableFuture<Map<Long, List<TagPO>>> getMappingByArticleIds(List<Long> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return CompletableFuture.completedFuture(new HashMap<>());
        }

        List<ArticleMtmTagPO> articleMtmTagPOList = lambdaQuery().in(ArticleMtmTagPO::getArticleId, articleIds).list();
        Map<Long, List<Long>> articleTagIdMapping = CommonUtils
                .getMapping(articleMtmTagPOList, ArticleMtmTagPO::getArticleId, ArticleMtmTagPO::getTagId);

        Set<Long> tagIds = articleTagIdMapping.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        LambdaQueryWrapper<TagPO> queryListWrapper = Wrappers.lambdaQuery(TagPO.class).in(TagPO::getId, tagIds);
        Map<Long, TagPO> tagPOMapping = tagMapper.selectList(queryListWrapper).stream()
                .collect(Collectors.toMap(TagPO::getId, Function.identity()));

        Map<Long, List<TagPO>> articleTagPOMapping = new HashMap<>();
        articleTagIdMapping.keySet().forEach(articleId -> {
            List<TagPO> tagPOList = articleTagIdMapping.get(articleId).stream()
                    .map(tagPOMapping::get)
                    .collect(Collectors.toList());
            articleTagPOMapping.put(articleId, tagPOList);
        });

        return CompletableFuture.completedFuture(articleTagPOMapping);
    }
}



