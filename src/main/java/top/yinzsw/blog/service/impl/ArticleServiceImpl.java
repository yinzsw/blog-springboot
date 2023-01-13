package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.ArticleMtmTagManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.ArticleArchiveVO;
import top.yinzsw.blog.model.vo.ArticleHomeVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.ArticleService;

import java.util.List;
import java.util.Map;
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
    public PageVO<ArticleArchiveVO> pageListArchives(PageReq pageReq) {
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
                .page(pageReq.getPager());

        List<ArticlePO> articlePOList = articlePOPage.getRecords();
        List<Long> categoryIds = articlePOList.stream().map(ArticlePO::getCategoryId).collect(Collectors.toList());
        Map<Long, String> categoryMapping = articleManager.getCategoryMappingByCategoryIds(categoryIds);

        List<Long> articleIds = articlePOList.stream().map(ArticlePO::getId).collect(Collectors.toList());
        Map<Long, List<TagPO>> tagMapping = articleMtmTagManager.getMappingByArticleIds(articleIds);

        List<ArticleHomeVO> articleHomeVOList = articleConverter.toArticleHomeVO(articlePOList, categoryMapping, tagMapping);
        return new PageVO<>(articleHomeVOList, articlePOPage.getTotal());
    }
}




