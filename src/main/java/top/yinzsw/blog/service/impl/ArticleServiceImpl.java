package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.ArticleMtmTagManager;
import top.yinzsw.blog.manager.RedisManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.dto.ArticleMappingDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.WebsiteConfigPO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;
import top.yinzsw.blog.model.vo.ArticleBackVO;
import top.yinzsw.blog.model.vo.ArticleHomeVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.ArticleService;
import top.yinzsw.blog.util.CommonUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2023-01-12 23:17:07
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticlePO> implements ArticleService {
    private final RedisManager redisManager;
    private final ArticleManager articleManager;
    private final ArticleMtmTagManager articleMtmTagManager;
    private final HttpContext httpContext;
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
        long totalCount = articlePOPage.getTotal();
        if (CollectionUtils.isEmpty(articlePOList)) {
            return new PageVO<>(List.of(), totalCount);
        }

        List<Long> categoryIds = articlePOList.stream().map(ArticlePO::getCategoryId).collect(Collectors.toList());
        List<Long> articleIds = articlePOList.stream().map(ArticlePO::getId).collect(Collectors.toList());
        List<ArticleHomeVO> articleHomeVOList = CommonUtils.biCompletableFuture(
                articleManager.getCategoryMappingByCategoryId(categoryIds),
                articleMtmTagManager.getMappingByArticleId(articleIds),
                (categoryMapping, tagMapping) -> articleConverter.toArticleHomeVO(articlePOList, categoryMapping, tagMapping));
        return new PageVO<>(articleHomeVOList, totalCount);
    }

    @Override
    public PageVO<ArticleBackVO> pageListBackArticles(PageReq pageReq, ArticleQueryReq articleQueryReq) {
        // 分页获取文章
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleCover, ArticlePO::getArticleStatus, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getIsDeleted, ArticlePO::getCreateTime)
                .eq(Objects.nonNull(articleQueryReq.getCategoryId()), ArticlePO::getCategoryId, articleQueryReq.getCategoryId())
                .eq(Objects.nonNull(articleQueryReq.getArticleStatus()), ArticlePO::getArticleStatus, articleQueryReq.getArticleStatus())
                .eq(Objects.nonNull(articleQueryReq.getArticleType()), ArticlePO::getArticleType, articleQueryReq.getArticleType())
                .in(Objects.nonNull(articleQueryReq.getTagId()), ArticlePO::getId, articleMtmTagManager.listArticleIdsByTagId(articleQueryReq.getTagId()))
                .like(Objects.nonNull(articleQueryReq.getKeywords()), ArticlePO::getArticleTitle, articleQueryReq.getKeywords())
                .page(pageReq.getPager());

        long totalCount = articlePOPage.getTotal();
        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        if (CollectionUtils.isEmpty(articlePOList)) {
            return new PageVO<>(Collections.emptyList(), totalCount);
        }

        //根据分类id列表获取分类信息 文章id列表获取标签信息
        List<Long> categoryIds = articlePOList.stream().map(ArticlePO::getCategoryId).collect(Collectors.toList());
        List<Long> articleIds = articlePOList.stream().map(ArticlePO::getId).collect(Collectors.toList());
        Map<Long, Long> articleLikeCount = redisManager.getArticleLikeCount(articleIds);
        Map<Long, Long> articleViewCount = redisManager.getArticleViewCount(articleIds);
        List<ArticleBackVO> articleBackVOList = CommonUtils.biCompletableFuture(
                articleManager.getCategoryMappingByCategoryId(categoryIds),
                articleMtmTagManager.getMappingByArticleId(articleIds),
                (categoryNameMapping, tagMapping) -> {
                    ArticleMappingDTO articleMappingDTO = new ArticleMappingDTO()
                            .setCategoryNameMapping(categoryNameMapping)
                            .setTagMapping(tagMapping)
                            .setLikeCountMapping(articleLikeCount)
                            .setViewCountMapping(articleViewCount);
                    return articleConverter.toArticleBackVO(articlePOList, articleMappingDTO);
                });

        return new PageVO<>(articleBackVOList, totalCount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateArticle(ArticleReq articleReq) {
        Long uid = httpContext.getCurrentClaimsDTO().getUid();
        //保存文章分类
        CategoryPO categoryPO = articleManager.saveArticleCategoryWileNotExist(articleReq.getCategoryName());
        //当设置文章封面使用文章默认封面
        if (!StringUtils.hasText(articleReq.getArticleCover())) {
            String articleCover = redisManager.getWebSiteConfig(WebsiteConfigPO::getArticleCover);
            articleReq.setArticleCover(articleCover);
        }

        //保存或更新文章
        ArticlePO articlePO = articleConverter.toArticlePO(articleReq, uid, categoryPO.getId());
        saveOrUpdate(articlePO);

        //保存文章标签
        return articleMtmTagManager.saveArticleTagsWileNotExist(articleReq.getTagNames(), articlePO.getId());
    }

    @Override
    public boolean updateArticleTop(Long articleId, Boolean isTop) {
        return lambdaUpdate().set(ArticlePO::getIsTop, isTop).eq(ArticlePO::getId, articleId).update();
    }
}




