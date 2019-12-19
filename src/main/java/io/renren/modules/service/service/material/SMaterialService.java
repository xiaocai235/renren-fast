package io.renren.modules.service.service.material;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.service.entity.material.SMaterialEntity;

import java.util.Map;

/**
 * 文件素材表，文件并不是真正存储到服务器当中，而是将文件上传到文件配置的云存储中
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-02 15:22:56
 */
public interface SMaterialService extends IService<SMaterialEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

