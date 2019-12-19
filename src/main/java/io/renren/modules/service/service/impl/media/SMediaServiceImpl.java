package io.renren.modules.service.service.impl.media;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.dao.meida.SMediaDao;
import io.renren.modules.service.entity.media.SMediaEntity;
import io.renren.modules.service.service.media.SMediaService;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;


@Service("sMediaService")
public class SMediaServiceImpl extends ServiceImpl<SMediaDao, SMediaEntity> implements SMediaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<SMediaEntity> wrapper = new QueryWrapper<>();

        if(params.containsKey("advertisingPlatform") && StringUtils.isNotEmpty((String) params.get("advertisingPlatform")))
        {
            wrapper = wrapper.like("advertising_platform",params.get("advertisingPlatform"));
        }
        if(params.containsKey("mediaType") && StringUtils.isNotEmpty((String) params.get("mediaType")))
        {
            wrapper = wrapper.like("media_type",params.get("mediaType"));
        }
        if(params.containsKey("mediaName") && StringUtils.isNotEmpty((String) params.get("mediaName")))
        {
            wrapper = wrapper.like("media_name",params.get("mediaName"));
        }
        if(params.containsKey("advertisingSpace") && StringUtils.isNotEmpty((String) params.get("advertisingSpace")))
        {
            wrapper = wrapper.like("advertising_space",params.get("advertisingSpace"));
        }


        if(params.containsKey("clientType") && StringUtils.isNotEmpty((String) params.get("clientType")))
        {
            wrapper = wrapper.eq("client_type",params.get("clientType"));
        }
        if(params.containsKey("advertisingSize") && StringUtils.isNotEmpty((String) params.get("advertisingSize")))
        {
            wrapper = wrapper.eq("advertising_size",params.get("advertisingSize"));
        }
        if(params.containsKey("trafficType") && StringUtils.isNotEmpty((String) params.get("trafficType")))
        {
            wrapper = wrapper.eq("traffic_type",params.get("trafficType"));
        }
        if(params.containsKey("advertisingSpaceType") && StringUtils.isNotEmpty((String) params.get("advertisingSpaceType")))
        {
            wrapper = wrapper.eq("advertising_space_type",params.get("advertisingSpaceType"));
        }
        if(params.containsKey("redirectType") && StringUtils.isNotEmpty((String) params.get("redirectType")))
        {
            wrapper = wrapper.eq("redirect_type",params.get("redirectType"));
        }
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        if(!userEntity.getUserId().equals(1))
        {
            wrapper.and(item -> item.eq("is_comment",1).or().eq("create_id",userEntity.getUserId()));
        }
        IPage<SMediaEntity> page = this.page(
                new Query<SMediaEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}