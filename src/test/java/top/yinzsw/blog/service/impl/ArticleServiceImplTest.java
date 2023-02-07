package top.yinzsw.blog.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.service.ArticleService;

@SpringBootTest
class ArticleServiceImplTest {
    private @Autowired ArticleService articleService;

    @Test
    void pageArticles() {
//        PageVO<ArticleDigestVO> articleDigestVOPageVO = articleService.pageArticles(new PageReq().setPage(1L).setSize(10L));
//        System.out.println(articleDigestVOPageVO);
    }
}