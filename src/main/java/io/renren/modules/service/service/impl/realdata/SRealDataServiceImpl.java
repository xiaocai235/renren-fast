package io.renren.modules.service.service.impl.realdata;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.dao.realdata.SRealDataDao;
import io.renren.modules.service.entity.realdata.InitCharLineRModule;
import io.renren.modules.service.entity.realdata.SRealDataEntity;
import io.renren.modules.service.service.realdata.SRealDataService;
import io.renren.modules.sys.entity.SysRoleEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysRoleService;
import io.renren.modules.sys.service.SysUserService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;


@Service("sRealDataService")
public class SRealDataServiceImpl extends ServiceImpl<SRealDataDao, SRealDataEntity> implements SRealDataService {

    @Autowired
    private SRealDataDao sRealDataDao;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<SRealDataEntity> wrapper = new QueryWrapper<>();

        Long userId = ShiroUtils.getUserId();
        //非admin ，角色授权为管理员
        if(!userId.equals(new Long(1)) && !params.containsKey("type"))
        {
            wrapper = wrapper.eq("create_id", ShiroUtils.getUserId());
        }

        //客户端
        if(params.containsKey("type") && String.valueOf(params.get("type")).equals("client"))
        {
            wrapper = wrapper.eq("user_id",ShiroUtils.getUserId());
        }
        else if(userId.equals(new Long(1)) && params.containsKey("userId"))
        {
            wrapper = wrapper.eq("user_id",params.get("userId"));
        }
        if(params.containsKey("jobName"))
        {
            wrapper = wrapper.like("job_name",params.get("jobName"));
        }
        if(params.containsKey("userName"))
        {
            wrapper = wrapper.like("user_name",params.get("userName"));
        }


        IPage<SRealDataEntity> page = this.page(
                new Query<SRealDataEntity>().getPage(params),
                wrapper.eq("del_flag",1)
        );

        return new PageUtils(page);
    }

    @Override
    public HashMap<String, BigDecimal> summary(Long userId) {
        return sRealDataDao.summary(userId);
    }

    /**
     * 统计
     * @param userId
     * @param sDate
     * @param eDate
     * @param isDay 如果为false 表示 day不参与分组，以 year，month 分组
     * @return
     */
    @Override
    public List<InitCharLineRModule> getClickAndExAndArriveByDay(Long userId, Date sDate, Date eDate,boolean isDay) {

        Integer sday = null;
        Integer eday = null;

        // 开始时间
        Calendar ca = Calendar.getInstance();
        ca.setTime(sDate);

        Integer smonth = ca.get(Calendar.MONTH)+1;
        Integer syear = ca.get(Calendar.YEAR);

        if(isDay)
        {
            sday = ca.get(Calendar.DAY_OF_MONTH);
        }

        //结束时间
        ca.setTime(eDate);

        Integer emonth = ca.get(Calendar.MONTH)+1;
        Integer eyear = ca.get(Calendar.YEAR);

        if(isDay)
        {
            eday = ca.get(Calendar.DAY_OF_MONTH);
        }
        return sRealDataDao.getClickAndExAndArriveByDay(userId,syear,smonth,sday,eyear,emonth,eday);
    }

}