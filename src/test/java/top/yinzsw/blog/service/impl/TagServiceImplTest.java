package top.yinzsw.blog.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagBackgroundSearchVO;
import top.yinzsw.blog.service.TagService;

@SpringBootTest
class TagServiceImplTest {
    private @Autowired TagService tagService;

    @Test
    void pageBackgroundSearchTags() {
        PageVO<TagBackgroundSearchVO> tagBackgroundSearchVOPageVO = tagService
                .pageBackgroundSearchTags(new PageReq(1L, 10L), "test");

    }
}