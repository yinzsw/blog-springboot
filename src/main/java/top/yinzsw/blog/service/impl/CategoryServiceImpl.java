package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.core.maps.MappingFactory;
import top.yinzsw.blog.mapper.CategoryMapper;
import top.yinzsw.blog.model.converter.CategoryConverter;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryDetailVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.CategoryService;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Service实现
 * @createDate 2023-01-13 09:57:14
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryPO> implements CategoryService {
    private final MappingFactory mappingFactory;
    private final CategoryConverter categoryConverter;

    @Override
    public PageVO<CategoryDetailVO> pageCategories(PageReq pageReq) {
        Page<CategoryPO> categoryPOPage = lambdaQuery()
                .select(CategoryPO::getId, CategoryPO::getCategoryName, CategoryPO::getCreateTime)
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(categoryPOPage);

        List<CategoryDetailVO> categoryDetailVOList = mappingFactory.getCategoryMapping(categoryPOPage.getRecords())
                .mapArticleCount().serialRun()
                .mappingList(categoryConverter::toCategoryDetailVO);
        return new PageVO<>(categoryDetailVOList, categoryPOPage.getTotal());
    }

    @Override
    public PageVO<CategoryVO> pageSearchCategories(PageReq pageReq, String keywords) {
        Page<CategoryPO> categoryPOPage = lambdaQuery()
                .select(CategoryPO::getId, CategoryPO::getCategoryName)
                .and(StringUtils.hasText(keywords), q -> q.apply(CategoryPO.FULL_MATCH, keywords))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(categoryPOPage);

        List<CategoryVO> categoryVOList = categoryConverter.toCategoryVO(categoryPOPage.getRecords());
        return new PageVO<>(categoryVOList, categoryPOPage.getTotal());
    }

    @Override
    public PageVO<CategoryDetailVO> pageBackgroundSearchCategories(PageReq pageReq, String keywords) {
        Page<CategoryPO> categoryPOPage = lambdaQuery()
                .select(CategoryPO::getId, CategoryPO::getCategoryName, CategoryPO::getCreateTime)
                .and(StringUtils.hasText(keywords), q -> q.apply(CategoryPO.FULL_MATCH, keywords))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(categoryPOPage);

        List<CategoryDetailVO> categoryDetailVOList = mappingFactory.getCategoryMapping(categoryPOPage.getRecords())
                .mapArticleCount().serialRun()
                .mappingList(categoryConverter::toCategoryDetailVO);
        return new PageVO<>(categoryDetailVOList, categoryPOPage.getTotal());
    }
}