package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.context.HttpContext;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.enums.TopicTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.manager.LikeManager;
import top.yinzsw.blog.manager.WebConfigManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.mapper.ArticleMtmTagMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.dto.ArticleMapsDTO;
import top.yinzsw.blog.model.dto.QueryBackgArticleDTO;
import top.yinzsw.blog.model.po.*;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.ArticleService;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.MapQueryUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2023-01-12 23:17:07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleMtmTagMapper articleMtmTagMapper;
    private final LikeManager likeManager;
    private final ArticleManager articleManager;
    private final CategoryManager categoryManager;
    private final WebConfigManager webConfigManager;
    private final HttpContext httpContext;
    private final UploadProvider uploadProvider;
    private final ArticleConverter articleConverter;

    @Override
    public ArticleVO getArticle(Long articleId) {
        // 查询文章信息
        ArticlePO articlePO = articleManager.getArticleById(articleId);
        Optional.ofNullable(articlePO).orElseThrow(() -> new BizException(String.format("id为%d的文章不存在", articleId)));

        // 更新浏览量
        String userIdentify = JwtManager.getCurrentContextDTO().map(Objects::toString)
                .or(httpContext::getUserIpAddress).orElse("unknown");
        articleManager.updateViewsInfo(articleId, userIdentify);

        // 查询上一篇和下一篇文章
        Long userId = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid).orElse(null);
        ArticlePO prevArticlePO = articleMapper.getPrevOrNextArticle(articleId, userId, true);
        ArticlePO nextArticlePO = articleMapper.getPrevOrNextArticle(articleId, userId, false);

        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(List.of(articlePO), true);
        return articleConverter.toArticleVO(articlePO, articleMapsDTO)
                .setPrevArticle(articleConverter.toArticleOutlineVO(prevArticlePO))
                .setNextArticle(articleConverter.toArticleOutlineVO(nextArticlePO));
    }

    @Override
    public ArticleBackgroundVO getBackgroundArticle(Long articleId) {
        ArticlePO articlePO = articleManager.getById(articleId);
        Optional.ofNullable(articlePO).orElseThrow(() -> new BizException(String.format("id为%d的文章不存在", articleId)));

        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(List.of(articlePO), false);
        return articleConverter.toArticleBackgroundVO(articlePO, articleMapsDTO);
    }

    @Override
    public List<ArticleSummaryVO> listRelatedArticles(Long articleId) {
        Long userId = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid).orElse(null);
        List<Long> tagIds = MapQueryUtils.create(ArticleMtmTagPO::getArticleId, List.of(articleId)).getValues(ArticleMtmTagPO::getTagId);
        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyList();
        }
        List<Long> articleIds = articleMtmTagMapper.listRelatedArticleIdsByTagIds(articleId, tagIds);
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyList();
        }
        List<ArticlePO> articlePOList = articleMapper.listRelatedArticleByIds(articleIds, userId);
        return articleConverter.toArticleSummaryVO(articlePOList);
    }

    @Override
    public PageVO<ArticleDigestVO> pageArticles(PageReq pageReq, Boolean isOnlyTop) {
        Long userId = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid).orElse(null);
        Page<ArticlePO> articlePOPage = articleMapper.pageArticles(pageReq.getPager(), userId, isOnlyTop);

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(articlePOList, false);
        List<ArticleDigestVO> articleDigestVOList = articleConverter.toArticleDigestVO(articlePOList, articleMapsDTO);
        return new PageVO<>(articleDigestVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(PageReq pageReq, ArticleQueryReq articleQueryReq) {
        List<Long> articleIds = Optional.ofNullable(articleQueryReq.getTagId())
                .map(tagId -> MapQueryUtils.create(ArticleMtmTagPO::getTagId, List.of(tagId)).getValues(ArticleMtmTagPO::getArticleId))
                .orElse(Collections.emptyList());

        QueryBackgArticleDTO queryBackgArticleDTO = articleConverter.toQueryBackgroundArticleDTO(articleQueryReq, articleIds);
        Page<ArticlePO> articlePOPage = articleMapper.pageBackgroundArticles(pageReq.getPager(), queryBackgArticleDTO);

        VerifyUtils.checkIPage(articlePOPage);

        //获取文章分类信息, 标签信息, 热度信息
        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(articlePOList, true);
        List<ArticleDigestBackgroundVO> articleDigestBackgroundVOList = articleConverter.toArticleDigestBackgroundVO(articlePOList, articleMapsDTO);
        return new PageVO<>(articleDigestBackgroundVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, Long categoryId) {
        Page<ArticlePO> articlePOPage = articleManager.pagePreviewArticlesByCategoryId(pageReq.getPager(), categoryId);

        VerifyUtils.checkIPage(articlePOPage);

        return getArticlePreviewVOPageVO(articlePOPage);
    }

    @Override
    public PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, List<Long> tagIds) {
        List<Long> articleIds = MapQueryUtils.create(ArticleMtmTagPO::getTagId, tagIds).getValues(ArticleMtmTagPO::getArticleId);
        Page<ArticlePO> articlePOPage = articleManager.pagePreviewArticlesByIds(pageReq.getPager(), articleIds);

        VerifyUtils.checkIPage(articlePOPage);

        return getArticlePreviewVOPageVO(articlePOPage);
    }

    @Override
    public PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq) {
        Page<ArticlePO> articlePOPage = articleManager.pageArchivesArticles(pageReq.getPager());

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticleArchiveVO> articleArchiveVOList = articleConverter.toArticleArchiveVO(articlePOPage.getRecords());
        return new PageVO<>(articleArchiveVOList, articlePOPage.getTotal());
    }

    @Override
    public PageVO<ArticleSearchVO> pageSearchArticles(PageReq pageReq, String keywords) {
        Long userId = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid).orElse(null);
        Page<ArticlePO> articlePOPage = articleMapper.pagePublicArticleByKeywords(pageReq.getPager(), keywords, userId);

        VerifyUtils.checkIPage(articlePOPage);

        List<ArticleSearchVO> articleSearchVOList = articleConverter.toArticleSearchVO(articlePOPage.getRecords());
        return new PageVO<>(articleSearchVOList, articlePOPage.getTotal());
    }

    @Override
    public String uploadFileArticleImage(MultipartFile image) {
        return uploadProvider.uploadFile(FilePathEnum.ARTICLE.getPath(), image);
    }

    @Override
    public boolean updateArticleIsLiked(Long articleId, Boolean like) {
        if (articleManager.count(Wrappers.<ArticlePO>lambdaQuery().eq(ArticlePO::getId, articleId)) == 0) {
            throw new BizException("未知的文章, 点赞失败");
        }

        likeManager.likeStrategy(TopicTypeEnum.ARTICLE, articleId, like);
        return like;
    }

    @Override
    public boolean updateArticleIsTop(Long articleId, Boolean isTop) {
        articleManager.updateById(new ArticlePO().setId(articleId).setIsTop(isTop));
        return isTop;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateArticle(ArticleReq articleReq) {
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        if (categoryManager.lambdaQuery().eq(CategoryPO::getId, articleReq.getCategoryId()).count() == 0) {
            throw new BizException("无效的文章分类id");
        }

        List<Long> existTagIds = MapQueryUtils.create(TagPO::getId, articleReq.getTagIds()).getValues(TagPO::getId);
        if (CollectionUtils.isEmpty(existTagIds)) {
            throw new BizException("无效的文章分类id");
        }

        //保存文章与标签的映射关系
        Db.saveBatch(CommonUtils.toList(existTagIds, tagId -> new ArticleMtmTagPO(articleReq.getId(), tagId)));

        //设置文章默认封面
        if (!StringUtils.hasText(articleReq.getArticleCover())) {
            String articleCover = webConfigManager.getWebSiteConfig(WebsiteConfigPO::getDefaultArticleCover);
            articleReq.setArticleCover(articleCover);
        }

        //保存或更新文章
        ArticlePO articlePO = articleConverter.toArticlePO(articleReq, uid);
        return articleManager.saveOrUpdate(articlePO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteArticles(List<Long> articleIds) {
        return articleManager.removeByIds(articleIds);
    }

    private PageVO<ArticlePreviewVO> getArticlePreviewVOPageVO(Page<ArticlePO> articlePOPage) {
        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(articlePOList, false);
        List<ArticlePreviewVO> articlePreviewVOList = articleConverter.toArticlePreviewVO(articlePOList, articleMapsDTO);
        return new PageVO<>(articlePreviewVOList, articlePOPage.getTotal());
    }
}




