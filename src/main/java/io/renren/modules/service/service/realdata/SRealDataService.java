package io.renren.modules.service.service.realdata;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.service.entity.realdata.InitCharLineRModule;
import io.renren.modules.service.entity.realdata.SRealDataEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据推广
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-04 09:56:34
 */
public interface SRealDataService extends IService<SRealDataEntity> {

    PageUtils queryPage(Map<String, Object> params);

    HashMap<String, BigDecimal> summary(Long userId);

    List<InitCharLineRModule> getClickAndExAndArriveByDay(Long userId, Date sDate,Date eDate,boolean isDay);
}

