package top.yinzsw.blog.manager;

import top.yinzsw.blog.enums.TopicTypeEnum;
import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.LikedPO;

/**
 * 点赞统一业务处理层
 *
 * @author yinzsW
 * @since 23/02/05
 */

public interface LikeManager extends CommonManager<LikedPO> {

    /**
     * 点赞策略
     *
     * @param topicType 主题类型
     * @param topicId   主题id
     * @param like      点赞/取消
     */
    void likeStrategy(TopicTypeEnum topicType, Long topicId, boolean like);
}
