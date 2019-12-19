package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.SysDictItemEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统字典项表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:41
 */
public interface SysDictItemService extends IService<SysDictItemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SysDictItemEntity> getDictItemListByDictName(String dictName);

}

