package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 首页文章
 *
 * @author yinzsW
 * @since 23/01/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "首页文章")
public class ArticleHomeVO {

    /**
     * 文章id
     */
    @Schema(title = "文章id")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(title = "文章标题")
    private String articleTitle;

    /**
     * 文章内容
     */
    @Schema(title = "文章内容")
    private String articleContent;

    /**
     * 文章缩略图
     */
    @Schema(title = "文章缩略图")
    private String articleCover;

    /**
     * 文章点赞量
     */
    @Schema(title = "文章点赞量")
    private Long likeCount;

    /**
     * 文章浏览量
     */
    @Schema(title = "文章浏览量")
    private Long viewsCount;

    /**
     * 文章类型
     */
    @Schema(title = "文章类型")
    private ArticleTypeEnum articleType;

    /**
     * 原文链接
     */
    @Schema(title = "原文链接")
    private String originalUrl;

    /**
     * 发表时间
     */
    @Schema(title = "发表时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;

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
     * 文章标签
     */
    @Schema(title = "文章标签")
    private List<TagVO> tags;

    /**
     * 上一篇文章
     */
    @Schema(title = "上一篇文章")
    private ArticleOutlineHomeVO prevArticle;

    /**
     * 下一篇文章
     */
    @Schema(title = "下一篇文章")
    private ArticleOutlineHomeVO nextArticle;

    /**
     * 相关文章推荐列表
     */
    @Schema(title = "相关文章推荐列表")
    private List<ArticleOutlineHomeVO> relatedRecommendArticles;

    /**
     * 最新文章推荐列表
     */
    @Schema(title = "最新文章推荐列表")
    private List<ArticleOutlineHomeVO> newestRecommendArticles;
}
