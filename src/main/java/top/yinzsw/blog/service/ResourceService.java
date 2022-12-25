package top.yinzsw.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yinzsw.blog.model.po.ResourcePO;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Service
 * @createDate 2022-12-15 15:00:20
 */
public interface ResourceService extends IService<ResourcePO> {

    /**
     * 根据唯一标识资源和请求方法查询资源
     *
     * @param uri    资源标识
     * @param method 请求方法
     * @return 资源
     */
    ResourcePO getResourceByUriAndMethod(String uri, String method);
}
