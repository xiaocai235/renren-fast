package io.renren.modules.service.controller.realdata;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.*;
import io.renren.modules.job.utils.ScheduleUtils;
import io.renren.modules.service.entity.capital.SCapitalEntity;
import io.renren.modules.service.entity.realdata.InitCharLineRModule;
import io.renren.modules.service.entity.realdata.InitChartLineModule;
import io.renren.modules.service.entity.realdata.SAreaDataEntity;
import io.renren.modules.service.entity.realdata.SRealDataEntity;
import io.renren.modules.service.service.capital.SCapitalService;
import io.renren.modules.service.service.realdata.SAreaDataService;
import io.renren.modules.service.service.realdata.SRealDataService;
import io.renren.modules.sys.entity.SysDictItemEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDictItemService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 数据推广
 *
 * @author chenshun
 * @email 2330016764@qq.com
 * @date 2019-07-04 09:56:34
 */
@RestController
@RequestMapping("generator/srealdata")
public class SRealDataController {
    @Autowired
    private SRealDataService sRealDataService;

    @Autowired
    private SCapitalService sCapitalService;

    @Autowired
    private SysDictItemService sysDictItemService;

    @Autowired
    private SAreaDataService areaDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Scheduler scheduler;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"generator:srealdata:list","generator:client-srealdata:list"},logical = Logical.OR)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sRealDataService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:srealdata:info")
    public R info(@PathVariable("id") Long id) {
        SRealDataEntity sRealData = sRealDataService.getById(id);

        return R.ok().put("sRealData", sRealData);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:srealdata:save")
    public R save(@RequestBody SRealDataEntity sRealData) throws JsonProcessingException {

        // 基本
        baseMethod(sRealData);

        if (checkSRealDataBeforeStart(sRealData)) return R.error("时间设置有误，请重新设置时间");

        // 0 检查剩余金额
        HashMap<String, BigDecimal> summary = sCapitalService.summary(sRealData.getUserId());
        BigDecimal shengyu = summary.get("shengyu");
        if(sRealData.getConsumeMoney().compareTo(shengyu) == 1)
        {
            return R.error("剩余金额不足，该用户剩余金额为："+shengyu+"，请充值或修改金额");
        }

        // 1 创建 消费记录
        SCapitalEntity capitalEntity = createCapitalEntity(sRealData);
        sCapitalService.save(capitalEntity);

        // 2 计算 各项数据
        sRealData.setCapitalId(capitalEntity.getId());
        compute(sRealData,false);

        //3 直接设置sRealdata的状态
        /*
         * REAL_DATA_STATUS
         * 开始 1
         * 正在运行 2
         * 结束 3
         * */
        sRealData.setStatu(1);

        // 4 存储 到数据库
        sRealDataService.save(sRealData);

        // 5 创建 定时任务
        createRealDataTask(sRealData);
        return R.ok();

    }
    @RequestMapping("/static")
    @RequiresPermissions("generator:srealdata:save")
    public R staticSave(@RequestBody SRealDataEntity sRealData) throws JsonProcessingException {
        baseMethod(sRealData);
        if (checkSRealDataBeforeStart(sRealData)) return R.error("时间设置有误，请重新设置时间");

        // 0 检查剩余金额
        HashMap<String, BigDecimal> summary = sCapitalService.summary(sRealData.getUserId());
        BigDecimal shengyu = summary.get("shengyu");
        if(sRealData.getConsumeMoney().compareTo(shengyu) == 1)
        {
            return R.error("剩余金额不足，该用户剩余金额为："+shengyu+"，请充值或修改金额");
        }

        // 1 创建 消费记录
        SCapitalEntity capitalEntity = createCapitalEntity(sRealData);
        capitalEntity.setMoney(sRealData.getConsumeMoney());
        sCapitalService.save(capitalEntity);


        // 2 计算 各项数据
        sRealData.setCapitalId(capitalEntity.getId());
        compute(sRealData,true);

        //3 直接设置sRealdata的状态
        /*
         * REAL_DATA_STATUS
         * 开始 1
         * 正在运行 2
         * 暂停 3
         * 结束 4
         * */
        sRealData.setStatu(4);

        // 4 存储 到数据
        boolean save = sRealDataService.save(sRealData);

        // 5 生成 点击，曝光，到达的地区分布信息
        HashMap<String, ArrayList<HashMap<String,Object>>> areaMap = generatorAreaMap(sRealData);

        // 6 地区信息存储到数据库
        SAreaDataEntity areaDataEntity = areaMapTransToAreaDataEntity(sRealData, areaMap);
        boolean save1 = areaDataService.save(areaDataEntity);
        if(save && save1)
        {
            return R.ok();
        }
        else
        {
            return R.error("操作失败!");
        }
    }

    private boolean checkSRealDataBeforeStart(SRealDataEntity sRealData) {
        // 检查时间是否合法
        BigDecimal halfMins = getHalfMins(sRealData.getStartTime(), sRealData.getEndTime());
        if(halfMins.intValue() < 1)
        {
            return true;
        }
        return false;
    }

    public SAreaDataEntity areaMapTransToAreaDataEntity(@RequestBody SRealDataEntity sRealData, HashMap<String, ArrayList<HashMap<String,Object>>> areaMap) throws JsonProcessingException {
        Long sRealDataId = sRealData.getId();
        SAreaDataEntity areaDataEntity = new SAreaDataEntity();
        areaDataEntity.setSRealDataId(sRealDataId);
        areaDataEntity.setData(objectMapper.writeValueAsString(areaMap));
        return areaDataEntity;
    }

    public HashMap<String, ArrayList<HashMap<String,Object>>> generatorAreaMap(SRealDataEntity sRealData)
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


    private void baseMethod(SRealDataEntity sRealData) {
        // 基本
        sRealData.setJobId(UUID.randomUUID().toString());
        Calendar calendar = Calendar.getInstance();
        sRealData.setCreateYear(calendar.get(Calendar.YEAR));
        sRealData.setCreateMonth(calendar.get(Calendar.MONTH) + 1);
        sRealData.setCreateDay(calendar.get(Calendar.DATE));

        SysUserEntity entity = ShiroUtils.getUserEntity();
        sRealData.setCreateId(entity.getUserId());
        sRealData.setCreateName(entity.getUsername());
        sRealData.setCreateTime(new Date());
    }


    private void createRealDataTask(SRealDataEntity sRealData) {
        ScheduleUtils.createScheduleJob(scheduler, sRealData, null);

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:srealdata:update")
    public R update(@RequestBody SRealDataEntity sRealData) {
        sRealDataService.updateById(sRealData);

        return R.ok();
    }

    /**
     * 暂停chartLine
     * @param id
     * @return
     * @throws SchedulerException
     */
    @RequestMapping("/stop")
    public R stop(@RequestBody Long id) throws SchedulerException {
        try {
            SRealDataEntity byId = sRealDataService.getById(id);
            JobKey jobKey = JobKey.jobKey(ScheduleUtils.JOB_NAME+byId.getJobId());
            scheduler.pauseJob(jobKey);
            byId.setStatu(3);
            sRealDataService.saveOrUpdate(byId);
            return R.ok();
        } catch (SchedulerException e) {
            return R.error();
        }
    }

    /**
     * 恢复
     * @param id
     * @return
     * @throws SchedulerException
     */
    @RequestMapping("/recovery")
    public R recovery(@RequestBody Long id) throws SchedulerException {
        SRealDataEntity byId = sRealDataService.getById(id);

        Date endTime = byId.getEndTime();
        if(endTime.getTime() < System.currentTimeMillis())
        {
            byId.setStatu(4);
            sRealDataService.saveOrUpdate(byId);
            SAreaDataEntity areaDataEntity = null;
            try {
                // 生成 点击，曝光，到达的地区分布信息
                HashMap<String, ArrayList<HashMap<String,Object>>> areaMap = generatorAreaMap(byId);

                // 地区信息存储到数据库
                areaDataEntity = areaMapTransToAreaDataEntity(byId, areaMap);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            areaDataService.save(areaDataEntity);
            return R.error("该任务已经结束");
        }
        else
        {
            byId.setStatu(3);
            sRealDataService.saveOrUpdate(byId);
            JobKey jobKey = JobKey.jobKey(ScheduleUtils.JOB_NAME+byId.getJobId());
            scheduler.resumeJob(jobKey);
            return R.ok();
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:srealdata:delete")
    public R delete(@RequestBody Long[] ids) throws SchedulerException {
        UpdateWrapper<SRealDataEntity> wrapper = new UpdateWrapper<>();
        for(Long item :ids)
        {
            SRealDataEntity byId = sRealDataService.getById(item);
            JobKey jobKey = JobKey.jobKey(ScheduleUtils.JOB_NAME+byId.getJobId());
            byId.setDelFlag(2);
            sRealDataService.update(byId,wrapper.eq("id",byId.getId()));
            scheduler.deleteJob(jobKey);
        }
        return R.ok();
    }

    @RequestMapping("/chartLine")
    public ArrayList<InitChartLineModule> test(@RequestParam(required = false) Long userId) throws ParseException {
        // 获取本周第一天和最后一天
        Date start = DateUtils.getWeekStart();
        Date end = DateUtils.getWeekEnd();
        List<InitCharLineRModule> rlist = checkUser(userId, start, end,true);
        int[] clicks = new int[7];
        int[] exposures = new int[7];
        int[] arrives = new int[7];

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        int[] weekDays = { 6, 0, 1, 2, 3, 4, 5};
        for(InitCharLineRModule item:rlist)
        {
            Date itemDate = format.parse(item.getCreateYear() + "-" + item.getCreateMonth() + "-" + item.getCreateDay());
            ca.setTime(itemDate);

            int wIndex = ca.get(Calendar.DAY_OF_WEEK) - 1;
            if(wIndex < 0) wIndex = 0;
            clicks[weekDays[wIndex]] = item.getClicks().intValue();
            exposures[weekDays[wIndex]] = item.getExposures().intValue();
            arrives[weekDays[wIndex]] = item.getArrives().intValue();
        }
        return getInitChartLineModules(clicks, exposures, arrives);
    }

    @RequestMapping("/chartLineM")
    public ArrayList<InitChartLineModule> chartLineM(@RequestParam(required = false) Long userId) {

        // 获取本月第一天和最后一天
        Date start = DateUtils.getMonthStart();
        Date end = DateUtils.getMonthEnd();

        List<InitCharLineRModule> rlist = checkUser(userId, start, end,true);

        int days = DateUtils.getCurrentMonthDay();

        int[] clicks = new int[days];
        int[] exposures = new int[days];
        int[] arrives = new int[days];

        for(InitCharLineRModule item:rlist)
        {

            clicks[item.getCreateDay()-1] = item.getClicks().intValue();
            exposures[item.getCreateDay()-1] = item.getExposures().intValue();
            arrives[item.getCreateDay()-1] = item.getArrives().intValue();

        }
        return getInitChartLineModules(clicks, exposures, arrives);
    }

    @RequestMapping("/area")
    @RequiresPermissions(value = {"generator:service:area","generator:client:area"},logical = Logical.OR)
    public R areaInfo(@RequestParam("realDataId") Long id)
    {
        SAreaDataEntity entity = areaDataService.queryByRealDataId(id);
        if(entity != null && entity.getData() != null && StringUtils.isNotEmpty(entity.getData()))
        {
            return R.ok().put("area",entity.getData());
        }
        else
        {
            return R.error("该条投放记录不存在地区分布信息");
        }
    }

    @RequestMapping("/chartLineY")
    public ArrayList<InitChartLineModule> chartLineY(@RequestParam(required = false) Long userId) throws ParseException {

        // 获取年度第一天和最后一天
        Date start = DateUtils.getYearStart();
        Date end = DateUtils.getYearEnd();

        List<InitCharLineRModule> rlist = checkUser(userId, start, end,false);
        int[] clicks = new int[12];
        int[] exposures = new int[12];
        int[] arrives = new int[12];

        for(InitCharLineRModule item:rlist)
        {

            clicks[item.getCreateMonth()-1] = item.getClicks().intValue();
            exposures[item.getCreateMonth()-1] = item.getExposures().intValue();
            arrives[item.getCreateMonth()-1] = item.getArrives().intValue();

        }
        return getInitChartLineModules(clicks, exposures, arrives);
    }

    //检查用户
    private List<InitCharLineRModule> checkUser(Long userId, Date start, Date end,boolean isDay) {
        if(userId == null)
        {
            userId = ShiroUtils.getUserId();
        }
        return sRealDataService.getClickAndExAndArriveByDay(userId, start, end,isDay);
    }
    // 拼装图表返回数据
    private ArrayList<InitChartLineModule> getInitChartLineModules(int[] clicks, int[] exposures, int[] arrives) {
        ArrayList<InitChartLineModule> result = new ArrayList<>();

        InitChartLineModule clickModule = new InitChartLineModule("点击量", "line", "总量", clicks);
        InitChartLineModule exposureModule = new InitChartLineModule("曝光量", "line", "总量", exposures);
        InitChartLineModule arriveModule = new InitChartLineModule("到达量", "line", "总量", arrives);

        result.add(clickModule);
        result.add(exposureModule);
        result.add(arriveModule);
        return result;
    }



    private SRealDataEntity compute(SRealDataEntity sRealDataEntity,boolean isStatic) {

        //花费金额
        BigDecimal consumeMoney = sRealDataEntity.getConsumeMoney();

        //到达率
        BigDecimal arrivesRate = sRealDataEntity.getArrivesRate();

        //cpc
        BigDecimal cpc = sRealDataEntity.getCpc();

        //cpm
        BigDecimal cpm = sRealDataEntity.getCpm();

        //点击总数
        BigDecimal clinks = consumeMoney.divide(cpc, 0, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(1));

        //曝光总数
        BigDecimal exposures = consumeMoney.multiply(new BigDecimal(1000)).divide(cpm, 0, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(1));

        //点击率
        BigDecimal clickRate = clinks.divide(exposures, 2, BigDecimal.ROUND_HALF_UP);

        //到达总数
        BigDecimal arrivers = clinks.multiply(arrivesRate).setScale(0, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(1));

        //半分钟数
        BigDecimal halfMins = getHalfMins(sRealDataEntity.getStartTime(), sRealDataEntity.getEndTime());

        //每次更新点击的次数
        BigDecimal oneUpdateClicks = clinks.divide(halfMins, 0, BigDecimal.ROUND_HALF_UP);

        //每次更新曝光的次数
        BigDecimal oneUpdateExposures = exposures.divide(halfMins, 0, BigDecimal.ROUND_HALF_UP);

        //每次更新到达的次数
        BigDecimal oneUpdateArrivers = arrivers.divide(halfMins, 0, BigDecimal.ROUND_HALF_UP);

        //每次更新花费的金额
        BigDecimal oneUpdateMoney = consumeMoney.divide(halfMins, 3, BigDecimal.ROUND_HALF_UP);


        sRealDataEntity.setOneUpdateClicks(oneUpdateClicks);
        sRealDataEntity.setOneUpdateExposures(oneUpdateExposures);
        sRealDataEntity.setOneUpdateArrives(oneUpdateArrivers);
        sRealDataEntity.setOneUpdateConsumeMoney(oneUpdateMoney);
        if(isStatic)
        {
            sRealDataEntity.setConsumeMoney(consumeMoney);
            sRealDataEntity.setClicks(clinks);
            sRealDataEntity.setClicksRate(clickRate);
            sRealDataEntity.setExposures(exposures);
            sRealDataEntity.setArrives(arrivers);
        }
        else
        {
            sRealDataEntity.setConsumeMoney(new BigDecimal(0));
            sRealDataEntity.setClicks(new BigDecimal(0));
            sRealDataEntity.setClicksRate(clickRate);
            sRealDataEntity.setExposures(new BigDecimal(0));
            sRealDataEntity.setArrives(new BigDecimal(0));
        }
        return sRealDataEntity;
    }


    private SCapitalEntity createCapitalEntity(SRealDataEntity sRealDataEntity) {

        sRealDataEntity.setEndMoney(sRealDataEntity.getConsumeMoney());

        SCapitalEntity entity = new SCapitalEntity();
        SysUserEntity userEntity = ShiroUtils.getUserEntity();

        entity.setUserId(sRealDataEntity.getUserId());
        entity.setUserName(sRealDataEntity.getUserName());
        entity.setMoney(new BigDecimal(0));

        entity.setClientType(sRealDataEntity.getClientType());
        entity.setCapitalType(1);
        entity.setRemark(sRealDataEntity.getJobName());

        entity.setCreateId(userEntity.getUserId());
        entity.setCreateName(userEntity.getUsername());
        entity.setCreateTime(sRealDataEntity.getCreateTime());

        entity.setCreateYear(String.valueOf(sRealDataEntity.getCreateYear()));
        entity.setCreateMonth(String.valueOf(sRealDataEntity.getCreateMonth()));
        entity.setCreateDay(String.valueOf(sRealDataEntity.getCreateDay()));

        return entity;

    }

    private BigDecimal getHalfMins(Date startTime, Date endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        long result = diff / 30 / 1000;
        return new BigDecimal(result);
    }

}
