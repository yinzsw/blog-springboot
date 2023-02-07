package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.mapper.CategoryMapper;
import top.yinzsw.blog.model.po.CategoryPO;

/**
 * 分类通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Service
public class CategoryManagerImpl extends ServiceImpl<CategoryMapper, CategoryPO> implements CategoryManager {

}
