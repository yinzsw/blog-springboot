package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.mapper.CategoryMapper;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.service.CategoryService;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Service实现
 * @createDate 2023-01-13 09:57:14
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryPO> implements CategoryService {

}