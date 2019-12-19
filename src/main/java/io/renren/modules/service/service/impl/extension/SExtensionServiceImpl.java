package io.renren.modules.service.service.impl.extension;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.dao.extension.SExtensionDao;
import io.renren.modules.service.entity.extension.SExtensionEntity;
import io.renren.modules.service.service.extension.SExtensionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("sExtensionService")
public class SExtensionServiceImpl extends ServiceImpl<SExtensionDao, SExtensionEntity> implements SExtensionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SExtensionEntity> wrapper = new QueryWrapper<>();
        if(params.containsKey("userName") && StringUtils.isNotEmpty((String) params.get("userName")))
        {
            wrapper = wrapper.like("user_name",params.get("userName"));
        }
        if(params.containsKey("extensionName")  && StringUtils.isNotEmpty((String) params.get("extensionName")))
        {
            wrapper = wrapper.like("extension_name",params.get("extensionName"));
        }
        if(params.containsKey("extensionType")  && StringUtils.isNotEmpty((String) params.get("extensionType")))
        {
            wrapper = wrapper.eq("extension_type",params.get("extensionType"));
        }
        Long userId = ShiroUtils.getUserId();
        if(!userId.equals(new Long(1)))
        {
            wrapper = wrapper.eq("create_id",ShiroUtils.getUserId());
        }

        IPage<SExtensionEntity> page = this.page(
                new Query<SExtensionEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}