package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.TagManager;
import top.yinzsw.blog.mapper.TagMapper;
import top.yinzsw.blog.model.po.TagPO;

/**
 * 标签通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/02/04
 */
@Service
public class TagManagerImpl extends ServiceImpl<TagMapper, TagPO> implements TagManager {
}
