package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.core.maps.MappingFactory;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.TagManager;
import top.yinzsw.blog.mapper.TagMapper;
import top.yinzsw.blog.model.converter.TagConverter;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.TagReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagBackgroundSearchVO;
import top.yinzsw.blog.model.vo.TagVO;
import top.yinzsw.blog.service.TagService;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Service实现
 * @createDate 2023-01-12 22:11:32
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, TagPO> implements TagService {
    private final TagManager tagManager;
    private final TagConverter tagConverter;
    private final MappingFactory mappingFactory;

    @Override
    public PageVO<TagVO> pageTags(PageReq pageReq) {
        Page<TagPO> tagPOPage = lambdaQuery().select(TagPO::getId, TagPO::getTagName).page(pageReq.getPager());

        VerifyUtils.checkIPage(tagPOPage);

        List<TagVO> tagVOList = tagConverter.toTagVO(tagPOPage.getRecords());
        return new PageVO<>(tagVOList, tagPOPage.getTotal());
    }

    @Override
    public PageVO<TagVO> pageSearchTags(PageReq pageReq, String keywords) {
        Page<TagPO> tagPOPage = lambdaQuery()
                .select(TagPO::getId, TagPO::getTagName)
                .and(StringUtils.hasText(keywords), q -> q.apply(TagPO.FULL_MATCH, keywords))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(tagPOPage);

        List<TagVO> tagVOList = tagConverter.toTagVO(tagPOPage.getRecords());
        return new PageVO<>(tagVOList, tagPOPage.getTotal());
    }

    @Override
    public PageVO<TagBackgroundSearchVO> pageBackgroundSearchTags(PageReq pageReq, String keywords) {
        Page<TagPO> tagPOPage = lambdaQuery()
                .select(TagPO::getId, TagPO::getTagName, TagPO::getCreateTime)
                .and(StringUtils.hasText(keywords), q -> q.apply(TagPO.FULL_MATCH, keywords))
                .page(pageReq.getPager());

        VerifyUtils.checkIPage(tagPOPage);

        List<TagBackgroundSearchVO> tagBackgroundSearchVOList = mappingFactory.getTagMapping(tagPOPage.getRecords())
                .mapArticleCount().serialRun()
                .mappingList(tagConverter::toTagSearchVO);
        return new PageVO<>(tagBackgroundSearchVOList, tagPOPage.getTotal());
    }

    @Override
    public boolean saveOrUpdateTag(TagReq tagReq) {
        TagPO existTagPO = lambdaQuery().select(TagPO::getId).eq(TagPO::getTagName, tagReq.getTagName()).one();
        if (Objects.nonNull(existTagPO)) {
            throw new BizException("标签名已存在");
        }

        TagPO tagPO = tagConverter.toTagPO(tagReq);
        return saveOrUpdate(tagPO);
    }

    @Override
    public boolean deleteTags(List<Long> tagIds) {
        boolean hasUseArticle = tagManager.hasUseArticle(tagIds);
        if (hasUseArticle) {
            throw new BizException("该标签下存在文章, 删除失败");
        }
        return lambdaUpdate().in(TagPO::getId, tagIds).remove();
    }
}




