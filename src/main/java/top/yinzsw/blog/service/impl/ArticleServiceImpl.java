package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.manager.WebConfigManager;
import top.yinzsw.blog.manager.mapping.ArticleMapping;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.WebsiteConfigPO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.ArticleService;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2023-01-12 23:17:07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticlePO> implements ArticleService {
    private final HttpContext httpContext;
    private final UserManager userManager;
    private final UploadProvider uploadProvider;
    private final ArticleMapping articleMapping;
    private final ArticleManager articleManager;
    private final WebConfigManager webConfigManager;
    private final ArticleConverter articleConverter;

    @Override
    public List<ArticleSearchVO> listSearchArticles(String keywords) {
        List<ArticlePO> articlePOList = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getArticleStatus, ArticlePO::getArticleContentDigest)
                .eq(ArticlePO::getIsDeleted, false)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .and(q -> q.apply("MATCH(article_title, article_content) AGAINST({0} IN BOOLEAN MODE)", keywords))
                .last("LIMIT 30")
                .list();
        return articleConverter.toArticleSearchVO(articlePOList);
    }

    @Override
    public PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq) {
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .eq(ArticlePO::getIsDeleted, false)
                .orderByDesc(ArticlePO::getCreateTime)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticleArchiveVO> articleArchiveVOS = articleConverter.toArticleArchiveVO(articlePOPage.getRecords());
        return new PageVO<>(articleArchiveVOS, articlePOPage.getTotal());
    }

    @Override
    public ArticleVO getArticle(Long articleId) {
        ArticlePO articlePO = getById(articleId);
        ArticleVO articleVO = articleMapping.builder(List.of(articlePO))
                .mapCategory(articlePO.getCategoryId()).mapTags(articleId).mapHotIndex(articleId).parallelBuild()
                .mappingOne(articleConverter::toArticleVO);

        //链式查询包装器
        LambdaQueryChainWrapper<ArticlePO> commonLambdaQuery = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleCover, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .eq(ArticlePO::getIsDeleted, false);

        // 查询上一篇和下一篇文章
        ArticlePO prevArticlePO = commonLambdaQuery.gt(ArticlePO::getId, articleId).orderByAsc(ArticlePO::getId).last("LIMIT 1").one();
        ArticlePO nextArticlePO = commonLambdaQuery.lt(ArticlePO::getId, articleId).orderByDesc(ArticlePO::getId).last("LIMIT 1").one();
        ArticleOutlineVO prevArticleOutlineVO = articleConverter.toArticleOutlineVO(prevArticlePO);
        ArticleOutlineVO nextArticleOutlineVO = articleConverter.toArticleOutlineVO(nextArticlePO);
        articleVO.setPrevArticle(prevArticleOutlineVO).setNextArticle(nextArticleOutlineVO);

        //查询最新文章
        List<ArticlePO> newestArticlesOf5 = commonLambdaQuery.orderByDesc(ArticlePO::getId).last("LIMIT 5").list();
        List<ArticleOutlineVO> newestRecommendVOList = articleConverter.toArticleOutlineVO(newestArticlesOf5);
        articleVO.setNewestRecommendArticles(newestRecommendVOList);

        //查询相关文章
        List<Long> relatedArticleIds = articleMapping.listRelatedArticleIds(articleId);
        if (!CollectionUtils.isEmpty(relatedArticleIds)) {
            List<ArticlePO> relatedArticlesOf6 = commonLambdaQuery.in(ArticlePO::getId, relatedArticleIds)
                    .orderByDesc(ArticlePO::getId).last("LIMIT 6").list();
            List<ArticleOutlineVO> relatedRecommendVOList = articleConverter.toArticleOutlineVO(relatedArticlesOf6);
            articleVO.setRelatedRecommendArticles(relatedRecommendVOList);
        }
        articleManager.updateViewsCount(articleId);
        return articleVO;
    }

    @Override
    public PageVO<ArticleDigestVO> pageArticles(PageReq pageReq, Boolean isTop) {
        // 分页获取文章
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleContentDigest, ArticlePO::getArticleCover, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getCreateTime)
                .eq(ArticlePO::getIsTop, isTop)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .eq(ArticlePO::getIsDeleted, false)
                .orderByDesc(ArticlePO::getId)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticleDigestVO> articleDigestVOList = articleMapping.builder(articlePOPage.getRecords())
                .mapCategory().mapTags().parallelBuild()
                .mappingList(articleConverter::toArticleDigestVO);
        return new PageVO<>(articleDigestVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, Long categoryId) {
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .eq(ArticlePO::getCategoryId, categoryId)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .eq(ArticlePO::getIsDeleted, false)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticlePreviewVO> articlePreviewVOList = articleMapping.builder(articlePOPage.getRecords())
                .mapCategory(categoryId).mapTags().parallelBuild()
                .mappingList(articleConverter::toArticlePreviewVO);

        return new PageVO<>(articlePreviewVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, List<Long> tagIds) {
        List<Long> articleIds = articleMapping.listArticleIds(tagIds.toArray(Long[]::new));

        Page<ArticlePO> articlePOPage = lambdaQuery()
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .eq(ArticlePO::getIsDeleted, false)
                .in(ArticlePO::getId, articleIds)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticlePreviewVO> articlePreviewVOList = articleMapping.builder(articlePOPage.getRecords())
                .mapCategory().mapTags(articleIds.toArray(Long[]::new)).parallelBuild()
                .mappingList(articleConverter::toArticlePreviewVO);
        return new PageVO<>(articlePreviewVOList, articlePOPage.getTotal());
    }

    @Override
    public ArticleBackgroundVO getBackgroundArticle(Long articleId) {
        ArticlePO articlePO = getById(articleId);
        Optional.ofNullable(articlePO).orElseThrow(() -> new BizException(String.format("id为%d的文章不存在", articleId)));

        return articleMapping.builder(List.of(articlePO))
                .mapCategory(articlePO.getId()).mapTags(articleId).parallelBuild()
                .mappingOne(articleConverter::toArticleBackgroundVO);
    }

    @Override
    public PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(PageReq pageReq, ArticleQueryReq articleQueryReq) {

        // 分页获取文章
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleCover, ArticlePO::getArticleStatus, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getIsDeleted, ArticlePO::getCreateTime)
                .eq(Objects.nonNull(articleQueryReq.getCategoryId()), ArticlePO::getCategoryId, articleQueryReq.getCategoryId())
                .eq(Objects.nonNull(articleQueryReq.getArticleStatus()), ArticlePO::getArticleStatus, articleQueryReq.getArticleStatus())
                .eq(Objects.nonNull(articleQueryReq.getArticleType()), ArticlePO::getArticleType, articleQueryReq.getArticleType())
                .eq(Objects.nonNull(articleQueryReq.getIsDeleted()), ArticlePO::getIsDeleted, articleQueryReq.getIsDeleted())
                .in(Objects.nonNull(articleQueryReq.getTagId()), ArticlePO::getId, articleMapping.listArticleIds(articleQueryReq.getTagId()))
                .like(Objects.nonNull(articleQueryReq.getKeywords()), ArticlePO::getArticleTitle, articleQueryReq.getKeywords())
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        //获取文章分类信息, 标签信息, 热度信息
        List<ArticleDigestBackgroundVO> articleDigestBackgroundVOList = articleMapping.builder(articlePOPage.getRecords())
                .mapCategory().mapTags().mapHotIndex().parallelBuild()
                .mappingList(articleConverter::toArticleDigestBackgroundVO);
        return new PageVO<>(articleDigestBackgroundVOList, articlePOPage.getTotal());
    }

    @Override
    public String uploadFileArticleImage(MultipartFile image) {
        return uploadProvider.uploadFile(FilePathEnum.ARTICLE.getPath(), image);
    }

    @Override
    public boolean likeArticle(Long articleId, Boolean like) {
        Long uid = httpContext.getCurrentContextDTO().getUid();
        String articleSid = articleId.toString();
        if (like && !userManager.isLikedArticle(uid, articleSid)) {
            userManager.saveLikedArticle(uid, articleSid);
            articleManager.updateLikeCount(articleId, 1L);
            return true;
        }

        if (!like && userManager.isLikedArticle(uid, articleSid)) {
            userManager.deleteLikedArticle(uid, articleSid);
            articleManager.updateLikeCount(articleId, -1L);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateArticleIsTop(Long articleId, Boolean isTop) {
        if (isTop) {
            List<ArticlePO> topArticlePOList = lambdaQuery().eq(ArticlePO::getIsTop, true).orderByDesc(ArticlePO::getUpdateTime).list();
            if (topArticlePOList.removeIf(articlePO -> articlePO.getId().equals(articleId)) && topArticlePOList.size() < 3) {
                return true;
            }

            if (topArticlePOList.size() >= 3) {
                topArticlePOList.removeIf(articlePO -> articlePO.getId().equals(articleId));
                List<Long> willCancelTopArticleIds = topArticlePOList.subList(2, topArticlePOList.size()).stream()
                        .map(ArticlePO::getId).collect(Collectors.toList());
                lambdaUpdate().set(ArticlePO::getIsTop, false).in(ArticlePO::getId, willCancelTopArticleIds).update();
            }
        }
        return lambdaUpdate().set(ArticlePO::getIsTop, isTop).eq(ArticlePO::getId, articleId).update();
    }

    @Override
    public boolean updateArticleIsDeleted(Long articleId, Boolean isDeleted) {
        return lambdaUpdate().set(ArticlePO::getIsDeleted, isDeleted).eq(ArticlePO::getId, articleId).update();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateArticle(ArticleReq articleReq) {
        //保存文章分类
        CategoryPO categoryPO = articleMapping.saveCategory(articleReq.getCategoryName());

        //当没有设置文章封面时使用文章默认封面
        if (!StringUtils.hasText(articleReq.getArticleCover())) {
            String articleCover = webConfigManager.getWebSiteConfig(WebsiteConfigPO::getArticleCover);
            articleReq.setArticleCover(articleCover);
        }

        //文章置顶
        updateArticleIsTop(articleReq.getId(), true);

        //保存或更新文章
        Long uid = httpContext.getCurrentContextDTO().getUid();
        ArticlePO articlePO = articleConverter.toArticlePO(articleReq, uid, categoryPO.getId());
        saveOrUpdate(articlePO);

        //保存文章标签
        return articleMapping.saveTagsAndMapping(articleReq.getTagNames(), articlePO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteArticles(List<Long> articleIds) {
        boolean isRemoveSuccess = lambdaUpdate().eq(ArticlePO::getIsDeleted, true).in(ArticlePO::getId, articleIds).remove();
        if (isRemoveSuccess) {
            articleMapping.deleteTagsMapping(articleIds);
        }
        return true;
    }
}




