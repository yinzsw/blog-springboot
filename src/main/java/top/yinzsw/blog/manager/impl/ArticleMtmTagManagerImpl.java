package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.ArticleMtmTagManager;
import top.yinzsw.blog.mapper.ArticleMtmTagMapper;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.util.MybatisPlusUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【article_mtm_tag(文章与标签映射表(多对多))】的数据库操作Service实现
 * @createDate 2023-01-13 09:59:04
 */
@Service
@RequiredArgsConstructor
public class ArticleMtmTagManagerImpl extends ServiceImpl<ArticleMtmTagMapper, ArticleMtmTagPO> implements ArticleMtmTagManager {

    @Async
    @Override
    public CompletableFuture<Map<Long, List<TagPO>>> getMappingByArticleId(List<Long> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return CompletableFuture.completedFuture(Collections.emptyMap());
        }

        Map<Long, List<Long>> articleTagIdMapping = MybatisPlusUtils.mappingGroup(ArticleMtmTagPO::getArticleId, ArticleMtmTagPO::getTagId, articleIds);
        Set<Long> tagIds = articleTagIdMapping.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Map<Long, TagPO> tagMapping = MybatisPlusUtils.mappingMap(TagPO::getId, tagIds);

        Map<Long, List<TagPO>> articleTagMapping = new HashMap<>();
        articleTagIdMapping.keySet().forEach(articleId -> {
            List<TagPO> tagPOList = articleTagIdMapping.get(articleId).stream().map(tagMapping::get).collect(Collectors.toList());
            articleTagMapping.put(articleId, tagPOList);
        });
        return CompletableFuture.completedFuture(articleTagMapping);
    }

    @Override
    public List<Long> listArticleIdsByTagId(Long tagId) {
        if (Objects.isNull(tagId)) {
            return null;
        }
        return MybatisPlusUtils.mappingList(ArticleMtmTagPO::getTagId, ArticleMtmTagPO::getArticleId, tagId);
    }
}



