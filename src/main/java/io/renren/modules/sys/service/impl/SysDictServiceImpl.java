package io.renren.modules.sys.service.impl;

import io.renren.modules.sys.dao.SysDictDao;
import io.renren.modules.sys.entity.SysDictEntity;
import io.renren.modules.sys.service.SysDictService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SysDictEntity> wrapper = new QueryWrapper<>();
        if(params.containsKey("dictName"))
        {
            wrapper =  wrapper.like("dict_name",params.get("dictName"));
        }
        if(params.containsKey("dictValue"))
        {
            wrapper =  wrapper.like("dict_value",params.get("dictValue"));
        }

        IPage<SysDictEntity> page = this.page(
                new Query<SysDictEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}