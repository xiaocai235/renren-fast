package io.renren.modules.service.service.webank;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.service.entity.webank.WebankEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-10-25 12:38:10
 */
public interface WebankService extends IService<WebankEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

