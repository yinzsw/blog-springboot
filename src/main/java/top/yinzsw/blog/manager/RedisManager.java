package top.yinzsw.blog.manager;

import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.model.dto.UserLikedDTO;

import java.util.List;
import java.util.Map;

/**
 * redis通用业务处理
 *
 * @author yinzsW
 * @since 23/01/15
 */

public interface RedisManager {

    /**
     * 保存邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void saveEmailVerificationCode(String email, String code);

    /**
     * 校验邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void checkEmailVerificationCode(String email, String code) throws BizException;

    /**
     * 根据用户id查找用户点赞信息
     *
     * @param userId 用户id
     * @return 点赞信息
     */
    UserLikedDTO getUserLikeInfo(Long userId);

    /**
     * 根据文章id获取文章点赞量
     *
     * @param articleIds 文章id列表
     * @return 文章id与点赞数映射表
     */
    Map<Long, Long> getArticleLikeCount(List<Long> articleIds);

    /**
     * 根据文章id获取文章浏览量
     *
     * @param articleIds 文章id列表
     * @return 文章id与浏览量映射表
     */
    Map<Long, Long> getArticleViewCount(List<Long> articleIds);

}