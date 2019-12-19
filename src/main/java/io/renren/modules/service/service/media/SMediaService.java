package io.renren.modules.service.service.media;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.service.entity.media.SMediaEntity;

import java.util.Map;

/**
 * 媒体
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-10 09:44:07
 */
public interface SMediaService extends IService<SMediaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

