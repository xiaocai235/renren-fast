package io.renren.modules.service.service.impl.realdata;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.service.dao.realdata.SAreaDataDao;
import io.renren.modules.service.entity.realdata.SAreaDataEntity;
import io.renren.modules.service.service.realdata.SAreaDataService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sAreaDataService")
public class SAreaDataServiceImpl extends ServiceImpl<SAreaDataDao, SAreaDataEntity> implements SAreaDataService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SAreaDataEntity> page = this.page(
                new Query<SAreaDataEntity>().getPage(params),
                new QueryWrapper<SAreaDataEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SAreaDataEntity queryByRealDataId(Long id) {

        QueryWrapper<SAreaDataEntity> sAreaDataEntityQuery = new QueryWrapper<>();

        if(id != null)
        {
            sAreaDataEntityQuery.eq("s_real_data_id",id);
        }
        return getOne(sAreaDataEntityQuery);
    }


}