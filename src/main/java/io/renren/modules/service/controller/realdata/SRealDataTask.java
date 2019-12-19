package io.renren.modules.service.controller.realdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.RandomUtils;
import io.renren.modules.job.entity.ScheduleJobEntity;
import io.renren.modules.job.utils.ScheduleUtils;
import io.renren.modules.service.entity.capital.SCapitalEntity;
import io.renren.modules.service.entity.realdata.SAreaDataEntity;
import io.renren.modules.service.entity.realdata.SRealDataEntity;
import io.renren.modules.service.service.capital.SCapitalService;
import io.renren.modules.service.service.realdata.SAreaDataService;
import io.renren.modules.service.service.realdata.SRealDataService;
import io.renren.modules.sys.entity.SysDictItemEntity;
import io.renren.modules.sys.service.SysDictItemService;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class SRealDataTask implements Job{


    //此处是解决无法注入的关键
    private static ApplicationContext applicationContext;
    //要注入的service或者dao
    public static void setApplicationContext(ApplicationContext applicationContext) {
        SRealDataTask.applicationContext = applicationContext;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        SRealDataService sRealDataService = applicationContext.getBean(SRealDataService.class);
        SCapitalService sCapitalService = applicationContext.getBean(SCapitalService.class);
        SysDictItemService sysDictItemService = applicationContext.getBean(SysDictItemService.class);
        SAreaDataService areaDataService = applicationContext.getBean(SAreaDataService.class);
        Scheduler scheduler = applicationContext.getBean(Scheduler.class);


        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        SRealDataEntity sRealDataEntity = (SRealDataEntity) jobDataMap.get(ScheduleJobEntity.JOB_PARAM_KEY);
        sRealDataEntity = sRealDataService.getById(  sRealDataEntity.getId());
        if(sRealDataEntity.getConsumeMoney().compareTo(sRealDataEntity.getEndMoney()) > -1)
        {
            JobKey jobKey = JobKey.jobKey(ScheduleUtils.JOB_NAME+sRealDataEntity.getJobId());
            try {
                scheduler.deleteJob(jobKey);
                beforeEndMethod(sysDictItemService, areaDataService, sRealDataEntity);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            sRealDataService.saveOrUpdate(sRealDataEntity);
            return;
        }
        else {
            // 更新 金额
            SCapitalEntity capitalEntity = sCapitalService.getById(sRealDataEntity.getCapitalId());
            BigDecimal moneyOne = capitalEntity.getMoney().add(sRealDataEntity.getOneUpdateConsumeMoney());
            capitalEntity.setMoney(moneyOne);
            BigDecimal moneyTwo = sRealDataEntity.getConsumeMoney().add(sRealDataEntity.getOneUpdateConsumeMoney());
            sRealDataEntity.setConsumeMoney(moneyTwo);


            // 更新 实时数据部分
            // 点击量
            sRealDataEntity.setClicks(sRealDataEntity.getClicks().add(sRealDataEntity.getOneUpdateClicks()));
            // 曝光量
            sRealDataEntity.setExposures(sRealDataEntity.getExposures().add(sRealDataEntity.getOneUpdateExposures()));
            // 到达量
            sRealDataEntity.setArrives(sRealDataEntity.getArrives().add(sRealDataEntity.getOneUpdateArrives()));
            sCapitalService.saveOrUpdate(capitalEntity);

            long endtime = sRealDataEntity.getEndTime().getTime();
            long current = System.currentTimeMillis();

            //更新 sRealdata的状态
            /*
             * REAL_DATA_STATUS
             * 开始 1
             * 正在运行 2
             * 暂停 3
             * 结束 4
             * */
            if(endtime-current > 30*1000)
            {
                sRealDataEntity.setStatu(2);
            }
            else
            {
                beforeEndMethod(sysDictItemService, areaDataService, sRealDataEntity);
            }
            sRealDataService.saveOrUpdate(sRealDataEntity);
        }

    }

    private void beforeEndMethod(SysDictItemService sysDictItemService, SAreaDataService areaDataService, SRealDataEntity sRealDataEntity) {
        // 实时数据运行结束
        sRealDataEntity.setStatu(4);

        try {
            HashMap<String, ArrayList<HashMap<String, Object>>> areaMap = this.generatorAreaMap(sysDictItemService, sRealDataEntity);
            SAreaDataEntity areaDataEntity = areaMapTransToAreaDataEntity(sRealDataEntity, areaMap);
            areaDataService.save(areaDataEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public SAreaDataEntity areaMapTransToAreaDataEntity(@RequestBody SRealDataEntity sRealData, HashMap<String, ArrayList<HashMap<String,Object>>> areaMap) throws JsonProcessingException {
        Long sRealDataId = sRealData.getId();
        SAreaDataEntity areaDataEntity = new SAreaDataEntity();
        areaDataEntity.setSRealDataId(sRealDataId);
        areaDataEntity.setData(new ObjectMapper().writeValueAsString(areaMap));
        return areaDataEntity;
    }
    public HashMap<String, ArrayList<HashMap<String,Object>>> generatorAreaMap(SysDictItemService sysDictItemService, SRealDataEntity sRealData)
    {

        HashMap<String, ArrayList<HashMap<String,Object>>> areaMap = new HashMap<String, ArrayList<HashMap<String,Object>>>();

        //点击
        ArrayList<HashMap<String,Object>> clicksList = new  ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> clicksMap = new HashMap<>();
        //到达
        ArrayList<HashMap<String,Object>> arriversList = new  ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> arriversMap = new HashMap<>();
        //曝光
        ArrayList<HashMap<String,Object>> exposuresList = new  ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> exposuresMap = new HashMap<>();
        //cpc
        BigDecimal cpc = sRealData.getCpc();
        //consumeMoney
        BigDecimal consumeMoney = sRealData.getConsumeMoney();
        //点击总数
        BigDecimal clinks = consumeMoney.divide(cpc, 0, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(1));


        //获取数据字典里面的省份信息
        List<SysDictItemEntity> province = sysDictItemService.getDictItemListByDictName("PROVINCE");



        //生成基本点击数组
        ArrayList<Integer> baseclicksList = RandomUtils.splitRedPacket(clinks.intValue(), province.size(), 0, clinks.intValue());
        //遍历省份信息，生成点击，到达，曝光，填充上面的三个Map
        for(int i=0;i<province.size();i++)
        {
            String provinceName= province.get(i).getDictItemKey();

            clicksMap.put("name",provinceName);
            arriversMap.put("name",provinceName);
            exposuresMap.put("name",provinceName);


            Integer itemClick = baseclicksList.get(i);
            clicksMap.put("value",itemClick);
            clicksList.add((HashMap<String, Object>) clicksMap.clone());

            BigDecimal itemArriver = new BigDecimal(itemClick).multiply(sRealData.getArrivesRate()).setScale(0, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(1));
            arriversMap.put("value",itemArriver);
            arriversList.add((HashMap<String, Object>) arriversMap.clone());

            BigDecimal itemConsumeMoney = new BigDecimal(itemClick).multiply(sRealData.getCpc());
            BigDecimal itemExposures = itemConsumeMoney.multiply(new BigDecimal(1000)).divide(sRealData.getCpm(), 0, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(1));
            exposuresMap.put("value",itemExposures);
            exposuresList.add((HashMap<String, Object>) exposuresMap.clone());


            clicksMap.clear();
            arriversMap.clear();
            exposuresMap.clear();
        }

        areaMap.put("clicks",clicksList);
        areaMap.put("arrivers",arriversList);
        areaMap.put("exposures",exposuresList);
        return areaMap;

    }
}
