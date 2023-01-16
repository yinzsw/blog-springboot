package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagVO;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Service
 * @createDate 2023-01-12 22:11:32
 */
public interface TagService extends IService<TagPO> {

    /**
     * 分页查询标签开列表
     *
     * @param pageReq 分页信息
     * @return 标签列表
     */
    PageVO<TagVO> pageTags(PageReq pageReq);
}
