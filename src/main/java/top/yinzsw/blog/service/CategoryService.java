package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryDetailVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Service
 * @createDate 2023-01-13 09:57:14
 */
public interface CategoryService extends IService<CategoryPO> {

    /**
     * 根据关键词查询分类信息
     *
     * @param pageReq 分页信息
     * @param name    分类名关键词
     * @return 分类列表
     */
    PageVO<CategoryVO> pageSearchCategories(PageReq pageReq, String name);

    /**
     * 根据关键词获取文章分类
     *
     * @param pageReq 分页信息
     * @param name    分类名关键词
     * @return 分类列表
     */
    PageVO<CategoryDetailVO> pageDetailCategories(PageReq pageReq, String name);

    /**
     * 保存或修改分类信息
     *
     * @param categoryReq 分类信息
     * @return 是否成功
     */
    boolean saveOrUpdateCategory(CategoryReq categoryReq);

    /**
     * 删除违章分类
     *
     * @param categoryIds 分类id列表
     * @return 是否成功
     */
    boolean deleteCategories(List<Long> categoryIds);
}