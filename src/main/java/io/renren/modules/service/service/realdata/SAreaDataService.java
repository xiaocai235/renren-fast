package io.renren.modules.service.service.realdata;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.service.entity.realdata.SAreaDataEntity;

import java.util.Map;

/**
 * 实时数据附表 主要用来存储地区分布数据
 *
 * @author chenshun
 * @email 2330016764@qq.com
 * @date 2019-07-19 10:15:22
 */
public interface SAreaDataService extends IService<SAreaDataEntity> {

    PageUtils queryPage(Map<String, Object> params);

    SAreaDataEntity queryByRealDataId(Long id);
}

