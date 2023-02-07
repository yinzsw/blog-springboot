package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.request.ArticleQueryReq;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
class ArticleManagerImplTest {
    private @Autowired ArticleManager articleManager;
    private @Autowired StringRedisTemplate stringRedisTemplate;

    @Test
    void getArticleMapsDTO() {
//        SetOperations<String, String> opsForSet = stringRedisTemplate.opsForSet();
//        String key = RedisConstEnum.VIEWED_ARTICLE_USERS_.getKey("54");
//        opsForSet.add(key, "5");
//        System.out.println(key);
//        System.out.println(opsForSet.isMember(key, "54"));
//
////        List<Object> objects = stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
////            @Override
////            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
////                operations.opsForSet().size((K) key);
////                return null;
////            }
////        });
////        System.out.println(objects);
//        ArticlePO articlePO1 = new ArticlePO().setId(54L).setCategoryId(187L);
//        ArticlePO articlePO2 = new ArticlePO().setId(58L).setCategoryId(201L);
//        List<ArticlePO> articlePO11 = List.of(articlePO2, articlePO1, articlePO2);
//        ArticleMapsDTO articleMapsDTO = articleManager.getArticleMapsDTO(articlePO11, true);
//        System.out.println(articleMapsDTO);


        LambdaQueryChainWrapper<ArticlePO> wrapper = articleManager.lambdaQuery().select(ArticlePO::getId).in(ArticlePO::getId, 1);
//        wrapper.
        LambdaQueryWrapper<ArticlePO> queryWrapper = Wrappers.lambdaQuery(ArticlePO.class).select(ArticlePO::getId).in(false, ArticlePO::getId, 1, 5, 5);
        System.out.println(queryWrapper.getSqlSegment());
        System.out.println(queryWrapper.getTargetSql());

        ArticleQueryReq articleQueryReq = new ArticleQueryReq()
                .setCategoryId(1l)
                .setArticleStatus(ArticleStatusEnum.PUBLIC)
                .setArticleType(ArticleTypeEnum.ORIGINAL);


        List<Integer> articleIds = List.of(1);
        String sqlSegment = Wrappers.<ArticlePO>lambdaQuery().allEq(new HashMap<>() {{
            put(ArticlePO::getCategoryId, articleQueryReq.getCategoryId());
            put(ArticlePO::getArticleStatus, articleQueryReq.getArticleStatus());
            put(ArticlePO::getArticleType, articleQueryReq.getArticleType());
        }}, false).in(!articleIds.isEmpty(), ArticlePO::getId, articleIds).getSqlSegment();

        System.out.println(sqlSegment);
    }
}