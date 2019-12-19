package io.renren.modules.service.dao.realdata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.service.entity.realdata.SAreaDataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实时数据附表 主要用来存储地区分布数据
 * 
 * @author chenshun
 * @email 2330016764@qq.com
 * @date 2019-07-19 10:15:22
 */
@Mapper
public interface SAreaDataDao extends BaseMapper<SAreaDataEntity> {
	
}
