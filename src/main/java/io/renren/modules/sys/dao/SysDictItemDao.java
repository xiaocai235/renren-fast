package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.sys.entity.SysDictItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统字典项表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:41
 */
@Mapper
public interface SysDictItemDao extends BaseMapper<SysDictItemEntity> {

    List<SysDictItemEntity> getDictItemListByDictName(String dictName);
}
