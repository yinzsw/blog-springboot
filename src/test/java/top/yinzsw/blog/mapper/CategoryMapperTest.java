package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.model.po.CategoryPO;

@SpringBootTest
class CategoryMapperTest {
    private @Autowired CategoryMapper categoryMapper;

    @Test
    void pageSearchCategories() {
        Page<CategoryPO> page = Page.of(1, 10);
        Page<CategoryPO> categoryPOPage = categoryMapper.pageSearchCategories(page, null);
    }
}