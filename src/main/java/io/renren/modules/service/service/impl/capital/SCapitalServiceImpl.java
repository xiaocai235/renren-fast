package io.renren.modules.service.service.impl.capital;

import com.mchange.lang.LongUtils;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.dao.capital.SCapitalDao;
import io.renren.modules.service.dao.capital.ViewSCapitalDao;
import io.renren.modules.service.entity.capital.SCapitalEntity;
import io.renren.modules.service.entity.capital.ViewSCapitalEntity;
import io.renren.modules.service.service.capital.SCapitalService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import org.springframework.util.ObjectUtils;


@Service("sCapitalService")
public class SCapitalServiceImpl extends ServiceImpl<SCapitalDao, SCapitalEntity> implements SCapitalService {

    @Autowired
    private SCapitalDao sCapitalDao;

    @Autowired
    private ViewSCapitalDao viewSCapitalDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SCapitalEntity> wrapper = new QueryWrapper<>();
        if(params.containsKey("userName") && StringUtils.isNotEmpty((String) params.get("userName")))
        {
            wrapper = wrapper.like("user_name",params.get("userName"));
        }
        if(params.containsKey("capitalType") && StringUtils.isNotEmpty((String) params.get("capitalType")))
        {
            wrapper = wrapper.eq("capital_type",params.get("capitalType"));
        }
        if(params.containsKey("clientType") && StringUtils.isNotEmpty((String) params.get("clientType")))
        {
            wrapper = wrapper.eq("client_type",params.get("clientType"));
        }
        if(params.containsKey("userId"))
        {
            wrapper = wrapper.eq("user_id",params.get("userId"));
        }
        Long userId = ShiroUtils.getUserId();
        if(!userId.equals(new Long(1)) && !params.containsKey("type"))
        {
            wrapper = wrapper.eq("create_id", ShiroUtils.getUserId());
        }
        //客户端
        if(params.containsKey("type") && String.valueOf(params.get("type")).equals("client"))
        {
            wrapper = wrapper.eq("user_id",ShiroUtils.getUserId());
        }

        IPage<SCapitalEntity> page = this.page(
                new Query<SCapitalEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public HashMap<String, BigDecimal> summary(Long userId) {

        List<ViewSCapitalEntity> summary = viewSCapitalDao.summary(userId);

        HashMap<String, BigDecimal> map = TranslateViewSCapital(summary);

        BigDecimal todayXiaofei = getTodayXiaofei(userId);
        if(todayXiaofei != null)
        {
            map.put("jinrixiaofei",todayXiaofei);
        }
        return map;
    }

    private BigDecimal getTodayXiaofei(Long userId)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);

        return sCapitalDao.getTodayXiaofei(userId,year,month,day);
    }

    private HashMap<String, BigDecimal> TranslateViewSCapital(List<ViewSCapitalEntity> summary) {

        HashMap<String, BigDecimal> map = new HashMap<>();

        BigDecimal xiaofei = new BigDecimal(0);
        BigDecimal chongzhi = new BigDecimal(0);
        BigDecimal shengyu = new BigDecimal(0);
        BigDecimal jinrixiaofei = new BigDecimal(0);

        boolean xiaofei_flag = false;
        boolean chongzhi_flag =false;

        for(ViewSCapitalEntity item:summary)
        {
            // 消费
            if(item.getCapitalType().equals(1))
            {
               xiaofei_flag = true;
               xiaofei = item.getMoney();
            }
            if(item.getCapitalType().equals(2))
            {
                chongzhi_flag = true;
                chongzhi = item.getMoney();
            }
        }

        if(xiaofei_flag == true && chongzhi_flag ==false)
        {
            shengyu = new BigDecimal(0).subtract(xiaofei);
        }
        else if(xiaofei_flag == true && chongzhi_flag == true)
        {
            shengyu = chongzhi.subtract(xiaofei);
        }
        else if(xiaofei_flag == false && chongzhi_flag == true)
        {
            shengyu = chongzhi.subtract(xiaofei);
        }

        map.put("chongzhi",chongzhi);
        map.put("xiaofei",xiaofei);
        map.put("shengyu",shengyu);
        map.put("jinrixiaofei",jinrixiaofei);

        return map;
    }

}