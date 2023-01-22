package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import top.yinzsw.blog.model.po.WebsiteConfigPO;

/**
 * 网站配置信息通用业务层
 *
 * @author yinzsW
 * @since 23/01/22
 */

public interface WebConfigManager {

    String WEBSITE_CONFIG = "blog:website:config";

    /**
     * 保存网站配置文件
     */
    void initWebSiteConfig();

    /**
     * 获取网站配置文件
     *
     * @return 配置文件
     */
    WebsiteConfigPO getWebSiteConfig();

    /**
     * 根据key策略从网站配置文件获取配置
     *
     * @param sFunction hashKey策略
     * @param <T>       原类型
     * @param <R>       返回类型
     * @return 返回配置
     */
    <T, R> R getWebSiteConfig(SFunction<T, R> sFunction);
}
