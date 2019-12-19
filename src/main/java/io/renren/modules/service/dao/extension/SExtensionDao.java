package io.renren.modules.service.dao.extension;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.service.entity.extension.SExtensionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 推广页面表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 17:25:40
 */
@Mapper
public interface SExtensionDao extends BaseMapper<SExtensionEntity> {
	
}
