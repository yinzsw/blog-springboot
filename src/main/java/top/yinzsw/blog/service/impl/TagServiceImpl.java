package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.mapper.TagMapper;
import top.yinzsw.blog.model.converter.TagConverter;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagVO;
import top.yinzsw.blog.service.TagService;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Service实现
 * @createDate 2023-01-12 22:11:32
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, TagPO> implements TagService {
    private final TagConverter tagConverter;

    @Override
    public PageVO<TagVO> pageListTags(PageReq pageReq) {
        Page<TagPO> tagPOPage = lambdaQuery()
                .select(TagPO::getId, TagPO::getTagName)
                .page(pageReq.getPager());
        List<TagVO> tagVOList = tagConverter.toTagVO(tagPOPage.getRecords());
        return new PageVO<>(tagVOList, tagPOPage.getTotal());
    }
}




