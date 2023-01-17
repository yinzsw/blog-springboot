package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.ArticleMtmTagManager;
import top.yinzsw.blog.manager.RedisManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.dto.ArticleHotIndexDTO;
import top.yinzsw.blog.model.dto.ArticleMappingDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.po.WebsiteConfigPO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.ArticleService;
import top.yinzsw.blog.util.CommonUtils;

import java.util.*;
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
    public PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq) {
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .eq(ArticlePO::getIsDeleted, false)
                .orderByDesc(ArticlePO::getCreateTime)
                .page(pageReq.getPager());

        List<ArticleArchiveVO> articleArchiveVOList = articleConverter.toArticleArchiveVO(articlePOPage.getRecords());
        return new PageVO<>(articleArchiveVOList, articlePOPage.getTotal());
    }

    @Override
    public ArticleHomeVO getHomeArticle(Long articleId) {
        ArticlePO articlePO = getById(articleId);
        CategoryPO categoryPO = articleManager.getCategory(articlePO.getCategoryId());
        List<TagPO> tags = articleMtmTagManager.getTags(articleId);
        ArticleHotIndexDTO articleHotIndexDTO = redisManager.getArticleHotIndex(articleId);
        ArticleHomeVO articleHomeVO = articleConverter.toArticleHomeVO(articlePO, categoryPO.getCategoryName(), tags, articleHotIndexDTO);

        //查询包装器
        LambdaQueryChainWrapper<ArticlePO> commonLambdaQuery = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getArticleCover, ArticlePO::getCreateTime)
                .eq(ArticlePO::getIsDeleted, false)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC);

        //查询相关文章
        List<Long> relatedArticleIds = articleMtmTagManager.getRelatedArticleIds(articleId);
        if (!CollectionUtils.isEmpty(relatedArticleIds)) {
            List<ArticlePO> relatedArticlesOf6 = commonLambdaQuery.in(ArticlePO::getId, relatedArticleIds)
                    .orderByDesc(ArticlePO::getId).last("LIMIT 6").list();
            List<ArticleOutlineHomeVO> relatedRecommendVOList = articleConverter.toArticleOutlineHomeVO(relatedArticlesOf6);
            articleHomeVO.setRelatedRecommendArticles(relatedRecommendVOList);
        }

        //查询最新文章
        List<ArticlePO> newestArticlesOf5 = commonLambdaQuery.orderByDesc(ArticlePO::getId).last("LIMIT 5").list();
        List<ArticleOutlineHomeVO> newestRecommendVOList = articleConverter.toArticleOutlineHomeVO(newestArticlesOf5);
        articleHomeVO.setNewestRecommendArticles(newestRecommendVOList);

        // 查询上一篇和下一篇文章
        ArticlePO prevArticlePO = commonLambdaQuery.gt(ArticlePO::getId, articleId).orderByAsc(ArticlePO::getId).last("LIMIT 1").one();
        ArticlePO nextArticlePO = commonLambdaQuery.lt(ArticlePO::getId, articleId).orderByDesc(ArticlePO::getId).last("LIMIT 1").one();
        ArticleOutlineHomeVO prevArticleOutlineHomeVO = articleConverter.toArticleOutlineHomeVO(prevArticlePO);
        ArticleOutlineHomeVO nextArticleOutlineHomeVO = articleConverter.toArticleOutlineHomeVO(nextArticlePO);
        return articleHomeVO.setPrevArticle(prevArticleOutlineHomeVO).setNextArticle(nextArticleOutlineHomeVO);
    }

    @Override
    public PageVO<ArticleDigestHomeVO> pageHomeArticles(PageReq pageReq) {
        // 分页获取文章
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleContent, ArticlePO::getArticleCover, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .eq(ArticlePO::getIsDeleted, false)
                .eq(ArticlePO::getIsTop, false)
                .orderByDesc(ArticlePO::getId)
                .page(pageReq.getPager());

        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        long totalCount = articlePOPage.getTotal();
        if (CollectionUtils.isEmpty(articlePOList)) {
            return new PageVO<>(List.of(), totalCount);
        }

        List<Long> categoryIds = articlePOList.stream().map(ArticlePO::getCategoryId).collect(Collectors.toList());
        List<Long> articleIds = articlePOList.stream().map(ArticlePO::getId).collect(Collectors.toList());
        List<ArticleDigestHomeVO> articleDigestHomeVOList = CommonUtils.biCompletableFuture(
                articleManager.getCategoryMappingByCategoryId(categoryIds), articleMtmTagManager.getMappingByArticleId(articleIds),
                (categoryMapping, tagMapping) -> articleConverter.toArticleDigestHomeVO(articlePOList, categoryMapping, tagMapping));
        return new PageVO<>(articleDigestHomeVOList, totalCount);
    }

    @Override
    public ArticleBackVO getBackArticle(Long articleId) {
        ArticlePO articlePO = getById(articleId);
        Optional.ofNullable(articlePO)
                .orElseThrow(() -> new BizException(String.format("id为%d的文章不存在", articleId)));

        CategoryPO categoryPO = articleManager.getCategory(articlePO.getCategoryId());
        List<TagPO> tagPOList = articleMtmTagManager.getTags(articleId);
        return articleConverter.toArticleBackVO(articlePO, categoryPO.getCategoryName(), tagPOList);
    }

    @Override
    public PageVO<ArticleDigestBackVO> pageBackArticles(PageReq pageReq, ArticleQueryReq articleQueryReq) {
        // 分页获取文章
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleCover, ArticlePO::getArticleStatus, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getIsDeleted, ArticlePO::getCreateTime)
                .eq(Objects.nonNull(articleQueryReq.getCategoryId()), ArticlePO::getCategoryId, articleQueryReq.getCategoryId())
                .eq(Objects.nonNull(articleQueryReq.getArticleStatus()), ArticlePO::getArticleStatus, articleQueryReq.getArticleStatus())
                .eq(Objects.nonNull(articleQueryReq.getArticleType()), ArticlePO::getArticleType, articleQueryReq.getArticleType())
                .eq(Objects.nonNull(articleQueryReq.getIsDeleted()), ArticlePO::getIsDeleted, articleQueryReq.getIsDeleted())
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
        Map<Long, ArticleHotIndexDTO> articleHotIndexMapping = redisManager.getArticleHotIndex(articleIds);
        List<ArticleDigestBackVO> articleDigestBackVOList = CommonUtils.biCompletableFuture(
                articleManager.getCategoryMappingByCategoryId(categoryIds), articleMtmTagManager.getMappingByArticleId(articleIds),
                (categoryNameMapping, tagMapping) -> {
                    ArticleMappingDTO articleMappingDTO = new ArticleMappingDTO(categoryNameMapping, tagMapping, articleHotIndexMapping);
                    return articleConverter.toArticleDigestBackVO(articlePOList, articleMappingDTO);
                });

        return new PageVO<>(articleDigestBackVOList, totalCount);
    }

    @Override
    public boolean updateArticleIsTop(Long articleId, Boolean isTop) {
        return lambdaUpdate().set(ArticlePO::getIsTop, isTop).eq(ArticlePO::getId, articleId).update();
    }

    @Override
    public boolean updateArticleIsDeleted(Long articleId, Boolean isDeleted) {
        return lambdaUpdate().set(ArticlePO::getIsDeleted, isDeleted).eq(ArticlePO::getId, articleId).update();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateArticle(ArticleReq articleReq) {
        Long uid = httpContext.getCurrentClaimsDTO().getUid();

        //保存文章分类
        CategoryPO categoryPO = articleManager.saveArticleCategoryWileNotExist(articleReq.getCategoryName());

        //当没有设置文章封面时使用文章默认封面
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteArticles(List<Long> articleIds) {
        boolean isRemoved = lambdaUpdate().eq(ArticlePO::getIsDeleted, true).in(ArticlePO::getId, articleIds).remove();
        return !isRemoved || articleMtmTagManager.deleteByArticleId(articleIds);
    }
}




