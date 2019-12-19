package io.renren.modules.service.dao.realdata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.service.entity.realdata.InitCharLineRModule;
import io.renren.modules.service.entity.realdata.SRealDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 数据推广
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-04 09:56:34
 */
@Mapper
public interface SRealDataDao extends BaseMapper<SRealDataEntity> {

    HashMap<String, BigDecimal> summary(Long userId);

    List<InitCharLineRModule> getClickAndExAndArriveByDay(Long userId, Integer syear, Integer smonth, Integer sday, Integer eyear, Integer emonth, Integer eday);
}
