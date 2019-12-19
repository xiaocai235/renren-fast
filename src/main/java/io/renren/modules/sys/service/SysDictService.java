package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.SysDictEntity;

import java.util.Map;

/**
 * 系统字典表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:42
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

