package io.renren.modules.service.service.impl.material;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.dao.material.SMaterialDao;
import io.renren.modules.service.entity.material.SMaterialEntity;
import io.renren.modules.service.service.material.SMaterialService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("sMaterialService")
public class SMaterialServiceImpl extends ServiceImpl<SMaterialDao, SMaterialEntity> implements SMaterialService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SMaterialEntity> wrapper = new QueryWrapper<>();
        Long userId = ShiroUtils.getUserId();
        if(!userId.equals(new Long(1))  && !params.containsKey("type"))
        {
            wrapper = wrapper.eq("create_id",ShiroUtils.getUserId());
        }

        //客户端
        if(params.containsKey("type") && String.valueOf(params.get("type")).equals("client"))
        {
            wrapper = wrapper.eq("user_id",ShiroUtils.getUserId());
        }

        if(params.containsKey("fileName") && StringUtils.isNotBlank((String) params.get("fileName")))
        {
            wrapper = wrapper.like("file_name", params.get("fileName"));
        }
        if(params.containsKey("userId") && StringUtils.isNotBlank((String) params.get("userId")))
        {
            wrapper = wrapper.like("user_id", params.get("userId"));
        }
        IPage<SMaterialEntity> page = this.page(
                new Query<SMaterialEntity>().getPage(params),wrapper

        );

        return new PageUtils(page);
    }

}