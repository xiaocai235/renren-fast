package io.renren.modules.sys.service.impl;

import io.renren.modules.sys.dao.SysDictItemDao;
import io.renren.modules.sys.entity.SysDictEntity;
import io.renren.modules.sys.entity.SysDictItemEntity;
import io.renren.modules.sys.service.SysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;


@Service("sysDictItemService")
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemDao, SysDictItemEntity> implements SysDictItemService {

    @Autowired
    private SysDictItemDao sysDictItemDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SysDictItemEntity> wrapper = new QueryWrapper<>();
        if(params.containsKey("dictId"))
        {
            wrapper = wrapper.eq("dict_id", params.get("dictId"));
        }
        if(params.containsKey("dictItemKey"))
        {
            wrapper = wrapper.like("dict_item_key", params.get("dictItemKey"));
        }
        IPage<SysDictItemEntity> queryPage = new Query<SysDictItemEntity>().getPage(params);
        if(params.containsKey("ispage") && String.valueOf(params.get("ispage")).equals("0"))
        {
            queryPage.setCurrent(0);
            queryPage.setSize(120);
        }
        IPage<SysDictItemEntity> page = this.page(
                queryPage,
                wrapper.orderByAsc("dict_id")
        );

        return new PageUtils(page);
    }

    @Override
    public List<SysDictItemEntity> getDictItemListByDictName(String dictName) {
        return sysDictItemDao.getDictItemListByDictName(dictName);
    }

}