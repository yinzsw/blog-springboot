package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.security.jwt.JwtContextDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.mapper.CategoryMapper;
import top.yinzsw.blog.model.converter.CategoryConverter;
import top.yinzsw.blog.model.dto.CategoryArticleNumDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryDetailVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.CategoryService;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Service实现
 * @createDate 2023-01-13 09:57:14
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final ArticleManager articleManager;
    private final CategoryManager categoryManager;
    private final CategoryConverter categoryConverter;

    @Override
    public PageVO<CategoryVO> pageSearchCategories(PageReq pageReq, String name) {
        Page<CategoryPO> categoryPOPage = categoryMapper.pageSearchCategories(pageReq.getPager(), name);

        VerifyUtils.checkIPage(categoryPOPage);

        List<CategoryVO> categoryVOList = categoryConverter.toCategoryVO(categoryPOPage.getRecords());
        return new PageVO<>(categoryVOList, categoryPOPage.getTotal());
    }

    @Override
    public PageVO<CategoryDetailVO> pageDetailCategories(PageReq pageReq, String name) {
        Page<CategoryPO> categoryPOPage = categoryMapper.pageSearchCategories(pageReq.getPager(), name);

        VerifyUtils.checkIPage(categoryPOPage);

        List<CategoryPO> categoryPOList = categoryPOPage.getRecords();
        List<Long> categoryIds = CommonUtils.toDistinctList(categoryPOList, CategoryPO::getId);
        Long uid = JwtManager.getCurrentContextDTO().map(JwtContextDTO::getUid).orElse(null);
        Map<Long, Long> articleCountMap = articleMapper.listCategoryArticleCount(categoryIds, uid).stream()
                .collect(Collectors.toMap(CategoryArticleNumDTO::getCategoryId, CategoryArticleNumDTO::getArticleCount));

        List<CategoryDetailVO> categoryDetailVOList = categoryConverter.toCategoryDetailVO(categoryPOList, articleCountMap);
        return new PageVO<>(categoryDetailVOList, categoryPOPage.getTotal());
    }

    @Override
    public CategoryVO saveOrUpdateCategory(CategoryReq categoryReq, Boolean repeatable) {
        CategoryPO existCategoryPO = categoryManager.lambdaQuery()
                .select(CategoryPO::getId, CategoryPO::getCategoryName)
                .eq(CategoryPO::getCategoryName, categoryReq.getCategoryName())
                .one();
        if (Objects.nonNull(existCategoryPO)) {
            return categoryConverter.toCategoryVO(existCategoryPO);
        }

        CategoryPO categoryPO = categoryConverter.toCategoryPO(categoryReq);
        categoryManager.saveOrUpdate(categoryPO);
        return categoryConverter.toCategoryVO(categoryPO);
    }

    @Override
    public boolean deleteCategories(List<Long> categoryIds) {
        if (articleManager.count(Wrappers.<ArticlePO>lambdaQuery().in(ArticlePO::getCategoryId, categoryIds)) > 0L) {
            throw new BizException("该分类下存在文章, 删除失败");
        }
        return categoryManager.removeByIds(categoryIds);
    }
}