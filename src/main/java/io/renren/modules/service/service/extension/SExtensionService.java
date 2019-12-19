package io.renren.modules.service.service.extension;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.service.entity.extension.SExtensionEntity;

import java.util.Map;

/**
 * 推广页面表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 17:25:40
 */
public interface SExtensionService extends IService<SExtensionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

