package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;
import top.yinzsw.blog.model.dto.QueryBackgArticleDTO;
import top.yinzsw.blog.model.po.ArticlePO;

@SpringBootTest
class ArticleMapperTest {
    private @Autowired ArticleMapper articleMapper;

    @Test
    void pageBackgroundArticles() {
        Page<Object> page = Page.of(1, 10);
        QueryBackgArticleDTO queryBackgArticleDTO = new QueryBackgArticleDTO()
                .setArticleType(ArticleTypeEnum.ORIGINAL)
                .setArticleStatus(ArticleStatusEnum.PUBLIC)
                .setArticleIds(null);
        Page<ArticlePO> articlePOPage = articleMapper.pageBackgroundArticles(page, queryBackgArticleDTO);
    }

    @Test
    void pageArticles() {
        Page<ArticlePO> page = Page.of(1, 10);
        articleMapper.pageArticles(page, 996L, null);
    }
}