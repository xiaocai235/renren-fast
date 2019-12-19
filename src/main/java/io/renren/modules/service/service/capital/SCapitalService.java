package io.renren.modules.service.service.capital;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.service.entity.capital.SCapitalEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 资金流水表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 12:47:32
 */
public interface SCapitalService extends IService<SCapitalEntity> {
    PageUtils queryPage(Map<String, Object> params);

    HashMap<String, BigDecimal> summary(Long userId);
}

