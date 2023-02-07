package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.FriendLinkManager;
import top.yinzsw.blog.mapper.FriendLinkMapper;
import top.yinzsw.blog.model.po.FriendLinkPO;

/**
 * 友链通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/02/05
 */
@Service
public class FriendLinkManagerImpl extends ServiceImpl<FriendLinkMapper, FriendLinkPO> implements FriendLinkManager {
}
