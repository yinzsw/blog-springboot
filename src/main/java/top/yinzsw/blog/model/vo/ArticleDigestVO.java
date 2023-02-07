package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 首页文章摘要
 *
 * @author yinzsW
 * @since 23/01/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "首页文章摘要")
public class ArticleDigestVO {

    /**
     * 文章id
     */
    @Schema(title = "文章id")
    private Long id;

    /**
     * 文章分类id
     */
    @Schema(title = "文章分类id")
    private Long categoryId;

    /**
     * 文章分类名
     */
    @Schema(title = "文章分类名")
    private String categoryName;

    /**
     * 文章标题
     */
    @Schema(title = "文章标题")
    private String articleTitle;

    /**
     * 文章内容
     */
    @Schema(title = "文章内容摘要")
    private String articleContent;

    /**
     * 文章缩略图
     */
    @Schema(title = "文章缩略图")
    private String articleCover;

    /**
     * 文章状态
     */
    @Schema(title = "文章状态")
    private ArticleStatusEnum articleStatus;

    /**
     * 文章类型
     */
    @Schema(title = "文章类型")
    private ArticleTypeEnum articleType;

    /**
     * 是否置顶
     */
    @Schema(title = "是否置顶")
    private Boolean isTop;

    /**
     * 发表时间
     */
    @Schema(title = "文章发表时间")
    private LocalDateTime createTime;

    /**
     * 文章标签列表
     */
    @Schema(title = "文章标签列表")
    private List<TagVO> tags;
}
