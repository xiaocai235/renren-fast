package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.sys.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统字典表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:42
 */
@Mapper
public interface SysDictDao extends BaseMapper<SysDictEntity> {
	
}
