package top.yinzsw.blog.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.enums.CommentOrderTypeEnum;

import java.util.List;

@SpringBootTest
class CommentMapperTest {
    private @Autowired CommentMapper commentMapper;

    @Test
    void getReplyCommentsMap() {
        var commentsMap = commentMapper.getGroupCommentDTO(List.of(725L, 728L), CommentOrderTypeEnum.DATE);
        System.out.println(commentsMap);
    }
}

