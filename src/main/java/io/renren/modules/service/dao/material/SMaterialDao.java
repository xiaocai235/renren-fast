package io.renren.modules.service.dao.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.service.entity.material.SMaterialEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件素材表，文件并不是真正存储到服务器当中，而是将文件上传到文件配置的云存储中
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-02 15:22:56
 */
@Mapper
public interface SMaterialDao extends BaseMapper<SMaterialEntity> {
	
}
