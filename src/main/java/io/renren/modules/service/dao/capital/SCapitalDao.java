package io.renren.modules.service.dao.capital;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.service.entity.capital.SCapitalEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;


/**
 * 资金流水表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 12:47:32
 */
@Mapper
public interface SCapitalDao extends BaseMapper<SCapitalEntity> {

    BigDecimal getTodayXiaofei(Long userId, Integer year,Integer month,Integer day);
}
