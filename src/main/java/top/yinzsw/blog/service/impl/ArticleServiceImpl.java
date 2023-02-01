package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.mybatisplus.SqlUtils;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.manager.WebConfigManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.WebsiteConfigPO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.ArticleService;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private final UserManager userManager;
    private final ArticleManager articleManager;
    private final CategoryManager categoryManager;
    private final WebConfigManager webConfigManager;
    private final UploadProvider uploadProvider;
    private final ArticleConverter articleConverter;

    @Override
    public List<ArticleSearchVO> listSearchArticles(String keywords) {
        List<ArticlePO> articlePOList = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getArticleStatus, ArticlePO::getArticleContentDigest)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .and(StringUtils.hasText(keywords), q -> q.apply(ArticlePO.FULL_MATCH, keywords))
                .last(SqlUtils.limit(16))
                .list();
        return articleConverter.toArticleSearchVO(articlePOList);
    }

    @Override
    public PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq) {
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .orderByDesc(ArticlePO::getCreateTime)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticleArchiveVO> articleArchiveVOS = articleConverter.toArticleArchiveVO(articlePOPage.getRecords());
        return new PageVO<>(articleArchiveVOS, articlePOPage.getTotal());
    }

    @Override
    public ArticleVO getArticle(Long articleId) {
        //更新浏览量
        articleManager.updateViewsCount(articleId);

        // 查询上一篇和下一篇文章
        ArticlePO prevArticlePO = getArticleOutlineCommonWrapper(1, true).gt(ArticlePO::getId, articleId).one();
        ArticlePO nextArticlePO = getArticleOutlineCommonWrapper(1, false).lt(ArticlePO::getId, articleId).one();

        //查询最新文章
        List<ArticlePO> newestArticles = getArticleOutlineCommonWrapper(5, false).list();

        //查询相关文章
        List<Long> relatedArticleIds = articleManager.listRelatedArticleIdsLimit6(articleId);
        List<ArticlePO> relatedArticles = CollectionUtils.isEmpty(relatedArticleIds) ? Collections.emptyList() :
                getArticleOutlineCommonWrapper(6, false).in(ArticlePO::getId, relatedArticleIds).list();

        ArticlePO articlePO = getById(articleId);
        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(List.of(articlePO), true);
        return articleConverter.toArticleVO(articlePO, articleMapsDTO)
                .setPrevArticle(articleConverter.toArticleOutlineVO(prevArticlePO))
                .setNextArticle(articleConverter.toArticleOutlineVO(nextArticlePO))
                .setNewestRecommendArticles(articleConverter.toArticleOutlineVO(newestArticles))
                .setRelatedRecommendArticles(articleConverter.toArticleOutlineVO(relatedArticles));
    }

    @Override
    public PageVO<ArticleDigestVO> pageArticles(PageReq pageReq, Boolean isTop) {
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleContentDigest, ArticlePO::getArticleCover, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getCreateTime)
                .eq(ArticlePO::getIsTop, isTop)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .orderByDesc(ArticlePO::getId)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(articlePOList, false);
        List<ArticleDigestVO> articleDigestVOList = articleConverter.toArticleDigestVO(articlePOList, articleMapsDTO);
        return new PageVO<>(articleDigestVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, Long categoryId) {
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .eq(ArticlePO::getCategoryId, categoryId)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .page(pageReq.getPager());

        return getArticlePreviewVOPageVO(articlePOPage);
    }

    @Override
    public PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, List<Long> tagIds) {
        List<Long> articleIds = articleManager.listArticleIds(tagIds);
        Page<ArticlePO> articlePOPage = lambdaQuery()
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .in(ArticlePO::getId, articleIds)
                .page(pageReq.getPager());

        return getArticlePreviewVOPageVO(articlePOPage);
    }

    @Override
    public ArticleBackgroundVO getBackgroundArticle(Long articleId) {
        ArticlePO articlePO = getById(articleId);
        Optional.ofNullable(articlePO).orElseThrow(() -> new BizException(String.format("id为%d的文章不存在", articleId)));

        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(List.of(articlePO), false);
        return articleConverter.toArticleBackgroundVO(articlePO, articleMapsDTO);
    }

    @Override
    public PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(PageReq pageReq, ArticleQueryReq articleQueryReq) {
        List<Long> articleIds = Optional.ofNullable(articleQueryReq.getTagId())
                .map(tagId -> articleManager.listArticleIds(Collections.singletonList(tagId)))
                .orElse(Collections.emptyList());
        String title = articleQueryReq.getTitle();


        Page<ArticlePO> articlePOPage = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getCategoryId, ArticlePO::getArticleTitle,
                        ArticlePO::getArticleCover, ArticlePO::getArticleStatus, ArticlePO::getArticleType,
                        ArticlePO::getIsTop, ArticlePO::getCreateTime)
                .allEq(new HashMap<>() {{
                    put(ArticlePO::getCategoryId, articleQueryReq.getCategoryId());
                    put(ArticlePO::getArticleStatus, articleQueryReq.getArticleStatus());
                    put(ArticlePO::getArticleType, articleQueryReq.getArticleType());
                }}, false)
                .in(!articleIds.isEmpty(), ArticlePO::getId, articleIds)
                .and(StringUtils.hasText(title), q -> q.apply(ArticlePO.FULL_MATCH_TITLE, title))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        //获取文章分类信息, 标签信息, 热度信息
        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(articlePOList, true);
        List<ArticleDigestBackgroundVO> articleDigestBackgroundVOList = articleConverter.toArticleDigestBackgroundVO(articlePOList, articleMapsDTO);
        return new PageVO<>(articleDigestBackgroundVOList, articlePOPage.getTotal());
    }

    @Override
    public String uploadFileArticleImage(MultipartFile image) {
        return uploadProvider.uploadFile(FilePathEnum.ARTICLE.getPath(), image);
    }

    @Override
    public boolean likeArticle(Long articleId, Boolean like) {
        Long uid = CommonUtils.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));


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
        if (Boolean.TRUE.equals(isTop)) {
            List<ArticlePO> topArticlePOList = lambdaQuery().eq(ArticlePO::getIsTop, true).orderByDesc(ArticlePO::getUpdateTime).list();
            if (topArticlePOList.removeIf(articlePO -> articlePO.getId().equals(articleId)) && topArticlePOList.size() < 3) {
                return true;
            }

            if (topArticlePOList.size() >= 3) {
                List<Long> willCancelTopArticleIds = topArticlePOList.subList(2, topArticlePOList.size()).stream()
                        .map(ArticlePO::getId).collect(Collectors.toList());
                lambdaUpdate().set(ArticlePO::getIsTop, false).in(ArticlePO::getId, willCancelTopArticleIds).update();
            }
        }
        return lambdaUpdate().eq(ArticlePO::getId, articleId).update(new ArticlePO().setIsTop(isTop));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateArticle(ArticleReq articleReq) {
        //设置文章默认封面
        if (!StringUtils.hasText(articleReq.getArticleCover())) {
            String articleCover = webConfigManager.getWebSiteConfig(WebsiteConfigPO::getDefaultArticleCover);
            articleReq.setArticleCover(articleCover);
        }

        //文章置顶
        Optional.ofNullable(articleReq.getIsTop()).ifPresent(isTop -> updateArticleIsTop(articleReq.getId(), isTop));
        articleReq.setIsTop(null);

        //保存文章分类
        Long categoryId = categoryManager.saveCategory(articleReq.getCategoryName()).getId();

        //保存或更新文章
        Long uid = CommonUtils.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        ArticlePO articlePO = articleConverter.toArticlePO(articleReq, categoryId, uid);
        saveOrUpdate(articlePO);

        //保存文章标签
        return articleManager.saveTagsMapping(articlePO.getId(), articleReq.getTagNames());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteArticles(List<Long> articleIds) {
        lambdaUpdate().in(ArticlePO::getId, articleIds).remove();
        return Db.lambdaUpdate(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getArticleId, articleIds).remove();
    }

    private LambdaQueryChainWrapper<ArticlePO> getArticleOutlineCommonWrapper(int limit, boolean isAsc) {
        return lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getArticleCover, ArticlePO::getCreateTime)
                .eq(ArticlePO::getArticleStatus, ArticleStatusEnum.PUBLIC)
                .orderBy(true, isAsc, ArticlePO::getId)
                .last(SqlUtils.limit(limit));
    }

    private PageVO<ArticlePreviewVO> getArticlePreviewVOPageVO(Page<ArticlePO> articlePOPage) {
        VerifyUtils.checkIPage(articlePOPage);

        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(articlePOList, false);
        List<ArticlePreviewVO> articlePreviewVOList = articleConverter.toArticlePreviewVO(articlePOList, articleMapsDTO);
        return new PageVO<>(articlePreviewVOList, articlePOPage.getTotal());
    }
}




