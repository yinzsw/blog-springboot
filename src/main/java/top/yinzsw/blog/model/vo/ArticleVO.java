package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import java.util.List;

/**
 * 文章
 *
 * @author yinzsW
 * @since 23/01/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章")
public class ArticleVO {

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
     * 文章分类
     */
    @Schema(title = "文章分类")
    private String categoryName;

    /**
     * 标题
     */
    @Schema(title = "文章标题")
    private String articleTitle;

    /**
     * 内容
     */
    @Schema(title = "文章内容")
    private String articleContent;

    /**
     * 文章封面
     */
    @Schema(title = "文章缩略图")
    private String articleCover;

    /**
     * 文章标签
     */
    @Schema(title = "文章标签")
    private List<TagVO> tags;

    /**
     * 文章类型
     */
    @Schema(title = "文章类型")
    private ArticleTypeEnum articleType;

    /**
     * 文章状态 1.公开 2.私密 3.评论可见
     */
    @Schema(title = "文章状态")
    private ArticleStatusEnum articleStatus;

    /**
     * 原文链接
     */
    @Schema(title = "原文链接")
    private String originalUrl;

    /**
     * 是否置顶
     */
    @Schema(title = "是否置顶")
    private Boolean isTop;
}
