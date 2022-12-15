package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.service.ResourceService;
import top.yinzsw.blog.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

/**
* @author yinzsW
* @description 针对表【resource(资源表)】的数据库操作Service实现
* @createDate 2022-12-15 15:00:20
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourcePO>
    implements ResourceService{

}




