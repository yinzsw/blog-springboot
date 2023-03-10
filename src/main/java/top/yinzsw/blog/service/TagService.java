package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.TagReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagBackgroundSearchVO;
import top.yinzsw.blog.model.vo.TagVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Service
 * @createDate 2023-01-12 22:11:32
 */
public interface TagService {

    /**
     * 根据关键词搜索文章标签
     *
     * @param pageReq 分页信息
     * @param name    标签名关键词
     * @return 标签列表
     */
    PageVO<TagVO> pageSearchTags(PageReq pageReq, String name);

    /**
     * 根据关键词搜索文章标签
     *
     * @param pageReq 分页信息
     * @param name    标签名关键词
     * @return 标签列表
     */
    PageVO<TagBackgroundSearchVO> pageBackgroundSearchTags(PageReq pageReq, String name);

    /**
     * 保存或更新标签
     *
     * @param tagReq     标签信息
     * @param repeatable 可重复
     * @return 是否成功
     */
    TagVO saveOrUpdateTag(TagReq tagReq, Boolean repeatable);

    /**
     * 删除标签
     *
     * @param tagIds 标签id列表
     * @return 是否成功
     */
    boolean deleteTags(List<Long> tagIds);
}
