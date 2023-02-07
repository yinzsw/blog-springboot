package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.TagManager;
import top.yinzsw.blog.mapper.ArticleMtmTagMapper;
import top.yinzsw.blog.mapper.TagMapper;
import top.yinzsw.blog.model.converter.TagConverter;
import top.yinzsw.blog.model.dto.TagArticleNumDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.TagReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagBackgroundSearchVO;
import top.yinzsw.blog.model.vo.TagVO;
import top.yinzsw.blog.service.TagService;
import top.yinzsw.blog.util.CommonUtils;
import top.yinzsw.blog.util.VerifyUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Service实现
 * @createDate 2023-01-12 22:11:32
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagMapper tagMapper;
    private final ArticleMtmTagMapper articleMtmTagMapper;
    private final TagManager tagManager;
    private final TagConverter tagConverter;

    @Override
    public PageVO<TagVO> pageSearchTags(PageReq pageReq, String name) {
        Page<TagPO> tagPOPage = tagMapper.pageSearchTags(pageReq.getPager(), name);

        VerifyUtils.checkIPage(tagPOPage);

        List<TagVO> tagVOList = tagConverter.toTagVO(tagPOPage.getRecords());
        return new PageVO<>(tagVOList, tagPOPage.getTotal());
    }

    @Override
    public PageVO<TagBackgroundSearchVO> pageBackgroundSearchTags(PageReq pageReq, String name) {
        Page<TagPO> tagPOPage = tagMapper.pageSearchTags(pageReq.getPager(), name);

        VerifyUtils.checkIPage(tagPOPage);

        List<TagPO> tagPOList = tagPOPage.getRecords();
        List<Long> tagIds = CommonUtils.toList(tagPOList, TagPO::getId);
        Map<Long, Long> articleCountMap = articleMtmTagMapper.listTagArticleCount(tagIds).stream()
                .collect(Collectors.toMap(TagArticleNumDTO::getTagId, TagArticleNumDTO::getArticleCount));

        List<TagBackgroundSearchVO> tagBackgroundSearchVOList = tagConverter.toTagSearchVO(tagPOList, articleCountMap);
        return new PageVO<>(tagBackgroundSearchVOList, tagPOPage.getTotal());
    }

    @Override
    public TagVO saveOrUpdateTag(TagReq tagReq, Boolean repeatable) {
        TagPO existTagPO = tagManager.lambdaQuery()
                .select(TagPO::getId, TagPO::getTagName)
                .eq(TagPO::getTagName, tagReq.getTagName())
                .one();
        if (Objects.nonNull(existTagPO)) {
            if (repeatable) {
                return tagConverter.toTagVO(existTagPO);
            }
            throw new BizException("标签名已存在");
        }

        TagPO tagPO = tagConverter.toTagPO(tagReq);
        tagManager.saveOrUpdate(tagPO);
        return tagConverter.toTagVO(tagPO);
    }

    @Override
    public boolean deleteTags(List<Long> tagIds) {
        if (articleMtmTagMapper.selectCount(Wrappers.<ArticleMtmTagPO>lambdaQuery().in(ArticleMtmTagPO::getTagId, tagIds)) > 0) {
            throw new BizException("该标签下存在文章, 删除失败");
        }
        return tagManager.removeByIds(tagIds);
    }
}




