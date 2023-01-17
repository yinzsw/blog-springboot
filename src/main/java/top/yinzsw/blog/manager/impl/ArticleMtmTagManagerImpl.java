package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.ArticleMtmTagManager;
import top.yinzsw.blog.mapper.ArticleMtmTagMapper;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.util.MybatisPlusUtils;

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
public class ArticleMtmTagManagerImpl extends ServiceImpl<ArticleMtmTagMapper, ArticleMtmTagPO> implements ArticleMtmTagManager {

    @Override
    public List<Long> getRelatedArticleIds(Long articleId) {
        List<Long> tagIds = MybatisPlusUtils.mappingList(ArticleMtmTagPO::getArticleId, ArticleMtmTagPO::getTagId, articleId);
        return Db.lambdaQuery(ArticleMtmTagPO.class)
                .ne(ArticleMtmTagPO::getArticleId, articleId)
                .in(ArticleMtmTagPO::getTagId, tagIds)
                .list().stream()
                .map(ArticleMtmTagPO::getArticleId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public CompletableFuture<Map<Long, List<TagPO>>> getMappingByArticleId(List<Long> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return CompletableFuture.completedFuture(Collections.emptyMap());
        }

        Map<Long, List<Long>> articleTagIdMapping = MybatisPlusUtils.mappingGroup(ArticleMtmTagPO::getArticleId, ArticleMtmTagPO::getTagId, articleIds);
        Set<Long> tagIds = articleTagIdMapping.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Map<Long, TagPO> tagMapping = MybatisPlusUtils.mappingMap(TagPO::getId, tagIds);

        Map<Long, List<TagPO>> articleTagMapping = articleTagIdMapping.keySet().stream().collect(Collectors.toMap(
                Function.identity(),
                articleId -> articleTagIdMapping.get(articleId).stream().map(tagMapping::get).collect(Collectors.toList())
        ));
        return CompletableFuture.completedFuture(articleTagMapping);
    }

    @Override
    public List<TagPO> getTags(Long articleId) {
        List<Long> tagIds = MybatisPlusUtils.mappingList(ArticleMtmTagPO::getArticleId, ArticleMtmTagPO::getTagId, articleId);
        return Db.lambdaQuery(TagPO.class).in(TagPO::getId, tagIds).list();
    }

    @Override
    public List<Long> listArticleIdsByTagId(Long tagId) {
        if (Objects.isNull(tagId)) {
            return null;
        }
        return MybatisPlusUtils.mappingList(ArticleMtmTagPO::getTagId, ArticleMtmTagPO::getArticleId, tagId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveArticleTagsWileNotExist(List<String> tagNames, Long articleId) {

        //获取未保存的标签名
        List<TagPO> existTagPOList = Db.lambdaQuery(TagPO.class).in(TagPO::getTagName, tagNames).list();
        List<TagPO> newTagPOList = tagNames.stream()
                .filter(tagName -> existTagPOList.stream().map(TagPO::getTagName).noneMatch(tagName::equalsIgnoreCase))
                .map(tageName -> new TagPO().setTagName(tageName))
                .collect(Collectors.toList());

        //保存新的标签名
        if (!CollectionUtils.isEmpty(newTagPOList)) {
            Db.saveBatch(newTagPOList);
        }

        //更新标签与文章映射关系
        deleteByArticleId(List.of(articleId));
        List<ArticleMtmTagPO> articleMtmTagPOList = newTagPOList.stream()
                .map(TagPO::getId)
                .collect(Collectors.toCollection(() -> existTagPOList.stream().map(TagPO::getId).collect(Collectors.toList()))).stream()
                .map(tagId -> new ArticleMtmTagPO(articleId, tagId))
                .collect(Collectors.toList());
        return saveBatch(articleMtmTagPOList);
    }

    @Override
    public boolean deleteByArticleId(List<Long> articleIds) {
        return Db.lambdaUpdate(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getArticleId, articleIds).remove();
    }
}



