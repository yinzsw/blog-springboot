package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryDetailVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Service
 * @createDate 2023-01-13 09:57:14
 */
public interface CategoryService extends IService<CategoryPO> {

    /**
     * 分页查询分类列表
     *
     * @param pageReq 分页信息
     * @return 分类列表
     */
    PageVO<CategoryDetailVO> pageCategories(PageReq pageReq);

    /**
     * 根据关键词查询分类信息
     *
     * @param pageReq  分页信息
     * @param keywords 关键词
     * @return 分类列表
     */
    PageVO<CategoryVO> pageSearchCategories(PageReq pageReq, String keywords);
}