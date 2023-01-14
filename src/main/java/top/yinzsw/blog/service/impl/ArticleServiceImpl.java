package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.exception.DaoException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.ArticleMtmTagManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.ArticleRequest;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;
import top.yinzsw.blog.model.vo.ArticleBackVO;
import top.yinzsw.blog.model.vo.ArticleHomeVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.ArticleService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2023-01-12 23:17:07
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticlePO> implements ArticleService {
    private final ArticleManager articleManager;
    private final ArticleMtmTagManager articleMtmTagManager;
    private final ArticleConverter articleConverter;

    @Override
    public PageVO<ArticleArchiveVO> pageListArchivesArticles(PageReq pageReq) {
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .orderByDesc(ArticlePO::getCreateTime)
                .page(pageReq.getPager());

        List<ArticleArchiveVO> articleArchiveVOList = articleConverter.toArticleArchiveVO(articlePOPage.getRecords());
        return new PageVO<>(articleArchiveVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticleHomeVO> pageListHomeArticles(PageReq pageReq) {
        // 分页获取文章
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleContent, ArticlePO::getArticleCover, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .orderByDesc(ArticlePO::getIsTop)
                .page(pageReq.getPager());

        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        if (CollectionUtils.isEmpty(articlePOList)) {
            return new PageVO<>(List.of(), articlePOPage.getTotal());
        }

        //根据分类id列表和文章id列表获取分类信息和标签信息
        List<Long> categoryIds = articlePOList.stream().map(ArticlePO::getCategoryId).collect(Collectors.toList());
        List<Long> articleIds = articlePOList.stream().map(ArticlePO::getId).collect(Collectors.toList());

        var categoryMappingFuture = articleManager.getCategoryMappingByCategoryIds(categoryIds);
        var tagMappingFuture = articleMtmTagManager.getMappingByArticleIds(articleIds);
        List<ArticleHomeVO> articleHomeVOList = CompletableFuture.allOf(categoryMappingFuture, tagMappingFuture)
                .thenApply(unused -> {
                    Map<Long, String> categoryMapping = categoryMappingFuture.join();
                    Map<Long, List<TagPO>> tagMapping = tagMappingFuture.join();
                    return articleConverter.toArticleHomeVO(articlePOList, categoryMapping, tagMapping);
                }).exceptionally(throwable -> {
                    throw new DaoException(throwable.getMessage());
                }).join();

        return new PageVO<>(articleHomeVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticleBackVO> pageListBackArticles(PageReq pageReq, ArticleRequest articleRequest) {
        List<ArticlePO> articlePOList = lambdaQuery()
                .select(ArticlePO::getId)
                .eq(Objects.nonNull(articleRequest.getCategoryId()), ArticlePO::getCategoryId, articleRequest.getCategoryId())
                .eq(Objects.nonNull(articleRequest.getArticleStatus()), ArticlePO::getArticleStatus, articleRequest.getArticleStatus())
                .eq(Objects.nonNull(articleRequest.getArticleType()), ArticlePO::getArticleType, articleRequest.getArticleType())
                .like(Objects.nonNull(articleRequest.getKeywords()), ArticlePO::getArticleTitle, articleRequest.getKeywords())
                .list();
        if (CollectionUtils.isEmpty(articlePOList)) {
            return new PageVO<>(List.of(), 0L);
        }

        List<Long> articleIds = articlePOList.stream().map(ArticlePO::getId).collect(Collectors.toList());
        return null;
    }
}




