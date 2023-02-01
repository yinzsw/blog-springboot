package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.mapper.CategoryMapper;
import top.yinzsw.blog.model.converter.CategoryConverter;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryDetailVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.CategoryService;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.MapQueryUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Map;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Service实现
 * @createDate 2023-01-13 09:57:14
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryPO> implements CategoryService {
    private final CategoryConverter categoryConverter;

    @Override
    public PageVO<CategoryVO> pageSearchCategories(PageReq pageReq, String name) {
        Page<CategoryPO> categoryPOPage = lambdaQuery()
                .select(CategoryPO::getId, CategoryPO::getCategoryName)
                .and(StringUtils.hasText(name), q -> q.apply(CategoryPO.FULL_MATCH, name))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(categoryPOPage);

        List<CategoryVO> categoryVOList = categoryConverter.toCategoryVO(categoryPOPage.getRecords());
        return new PageVO<>(categoryVOList, categoryPOPage.getTotal());
    }

    @Override
    public PageVO<CategoryDetailVO> pageDetailCategories(PageReq pageReq, String name) {
        Page<CategoryPO> categoryPOPage = lambdaQuery()
                .select(CategoryPO::getId, CategoryPO::getCategoryName, CategoryPO::getCreateTime)
                .and(StringUtils.hasText(name), q -> q.apply(CategoryPO.FULL_MATCH, name))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(categoryPOPage);

        List<CategoryPO> categoryPOList = categoryPOPage.getRecords();
        List<Long> categoryIds = CommonUtils.toList(categoryPOList, CategoryPO::getId);
        Map<Long, Long> articleCountMap = MapQueryUtils.create(ArticlePO::getCategoryId, categoryIds)
                .queryWrapper(wrapper -> wrapper.groupBy(ArticlePO::getCategoryId))
                .getKeyValueMap(ArticlePO::getArticleCount);
        List<CategoryDetailVO> categoryDetailVOList = categoryConverter.toCategoryDetailVO(categoryPOList, articleCountMap);
        return new PageVO<>(categoryDetailVOList, categoryPOPage.getTotal());
    }

    @Override
    public boolean saveOrUpdateCategory(CategoryReq categoryReq) {
        lambdaQuery().select(CategoryPO::getId).eq(CategoryPO::getCategoryName, categoryReq.getCategoryName()).oneOpt()
                .ifPresent(categoryPO -> {
                    throw new BizException("分类名已存在");
                });

        CategoryPO categoryPO = categoryConverter.toCategoryPO(categoryReq);
        return saveOrUpdate(categoryPO);
    }

    @Override
    public boolean deleteCategories(List<Long> categoryIds) {
        boolean isUsing = Db.lambdaQuery(ArticlePO.class).in(ArticlePO::getCategoryId, categoryIds).count() > 0L;
        if (isUsing) {
            throw new BizException("该分类下存在文章, 删除失败");
        }
        return lambdaUpdate().in(CategoryPO::getId, categoryIds).remove();
    }
}