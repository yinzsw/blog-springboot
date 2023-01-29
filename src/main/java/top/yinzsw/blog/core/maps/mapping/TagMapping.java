package top.yinzsw.blog.core.maps.mapping;

import top.yinzsw.blog.core.maps.handler.MapTaskRunnerTemplate;
import top.yinzsw.blog.core.maps.util.MapQueryUtils;
import top.yinzsw.blog.model.dto.TagMapsDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 标签映射
 *
 * @author yinzsW
 * @since 23/01/25
 */
public final class TagMapping extends MapTaskRunnerTemplate<TagPO, TagMapsDTO> {

    public TagMapping(Executor executor, List<TagPO> originList) {
        super(executor, originList, new TagMapsDTO());
    }

    private List<Long> getTagIds() {
        return getOriginList().stream().map(TagPO::getId).collect(Collectors.toList());
    }

    public TagMapping mapArticleCount() {
        addRunnable(() -> {
            List<Long> tagIds = getTagIds();
            Map<Long, Long> article2CountMap = MapQueryUtils.create(ArticleMtmTagPO::getTagId, tagIds)
                    .queryWrapper(wrapper -> wrapper.groupBy(ArticleMtmTagPO::getTagId))
                    .getKeyValueMap(ArticleMtmTagPO::getArticleCount);
            getContextDTO().setArticleCountMap(article2CountMap);
        });
        return this;
    }
}
