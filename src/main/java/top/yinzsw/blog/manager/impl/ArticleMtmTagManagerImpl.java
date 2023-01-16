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
    public List<Long> listArticleIdsByTagId(Long tagId) {
        if (Objects.isNull(tagId)) {
            return null;
        }
        return MybatisPlusUtils.mappingList(ArticleMtmTagPO::getTagId, ArticleMtmTagPO::getArticleId, tagId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveArticleTagsWileNotExist(List<String> tagNames, Long articleId) {
        //清除已经存在的标签名
        List<TagPO> existTagPOList = Db.lambdaQuery(TagPO.class).in(TagPO::getTagName, tagNames).list();
        if (!CollectionUtils.isEmpty(existTagPOList)) {
            List<String> existTagNames = existTagPOList.stream().map(TagPO::getTagName).collect(Collectors.toList());
            tagNames.removeAll(existTagNames);
        }

        //保存标签
        List<TagPO> tagPOList = tagNames.stream().map(tageName -> new TagPO().setTagName(tageName)).collect(Collectors.toList());
        Db.saveBatch(tagPOList);

        //将标签与文章建立映射关系
        List<ArticleMtmTagPO> articleMtmTagPOList = tagPOList.stream()
                .map(tagPO -> new ArticleMtmTagPO(articleId, tagPO.getId()))
                .collect(Collectors.toList());
        return saveBatch(articleMtmTagPOList);
    }

    @Override
    public boolean deleteByArticleId(List<Long> articleIds) {
        return Db.lambdaUpdate(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getArticleId, articleIds).remove();
    }

    @Override
    public List<TagPO> getTags(Long articleId) {
        List<Long> tagIds = MybatisPlusUtils.mappingList(ArticleMtmTagPO::getArticleId, ArticleMtmTagPO::getTagId, articleId);
        return Db.lambdaQuery(TagPO.class).in(TagPO::getId, tagIds).list();
    }
}



