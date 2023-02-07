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
public class ArticleVO {

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
    private Long likesCount;

    /**
     * 文章浏览量
     */
    @Schema(title = "文章浏览量")
    private Long viewsCount;

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
     * 当日热度信息
     */
    @Schema(title = "当日热度信息")
    private ArticleDayHotVO dayHot;

    /**
     * 文章标签
     */
    @Schema(title = "文章标签")
    private List<TagVO> tags;

    /**
     * 上一篇文章
     */
    @Schema(title = "上一篇文章")
    private ArticleOutlineVO prevArticle;

    /**
     * 下一篇文章
     */
    @Schema(title = "下一篇文章")
    private ArticleOutlineVO nextArticle;
}
