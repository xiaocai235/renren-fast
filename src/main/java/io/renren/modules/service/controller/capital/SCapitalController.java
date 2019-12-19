package io.renren.modules.service.controller.capital;

import java.math.BigDecimal;
import java.util.*;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.entity.capital.SCapitalEntity;
import io.renren.modules.service.service.capital.SCapitalService;
import io.renren.modules.service.service.realdata.SRealDataService;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 资金流水表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 12:47:32
 */
@RestController
@RequestMapping("generator/scapital")
public class SCapitalController {
    @Autowired
    private SCapitalService sCapitalService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SRealDataService sRealDataService;



    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:scapital:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sCapitalService.queryPage(params);
        return R.ok().put("page", page);
    }

    @RequestMapping("/info")
    @RequiresPermissions(value = {"generator:scapital:info","generator:client:info"},logical = Logical.OR)
    public R info(@RequestParam Map<String, Object> params) {

        Long userId = ShiroUtils.getUserId();
        params.put("userId", userId);
        PageUtils page = sCapitalService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:scapital:info")
    public R info(@PathVariable("id") Long id) {
        SCapitalEntity sCapital = sCapitalService.getById(id);

        return R.ok().put("sCapital", sCapital);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:scapital:save")
    public R save(@RequestBody SCapitalEntity sCapital) {
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        sCapital.setCreateId(userEntity.getUserId());
        sCapital.setCreateName(userEntity.getUsername());

        Calendar calendar = Calendar.getInstance();
        sCapital.setCreateYear(String.valueOf(calendar.get(Calendar.YEAR)));
        sCapital.setCreateMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));
        sCapital.setCreateDay(String.valueOf(calendar.get(Calendar.DATE)));

        Long userId = ShiroUtils.getUserId();
        //非admin账户
        if(!userId.equals(new Long(1)))
        {

            if(sCapital.getCapitalType().equals(2))
            {
                HashMap<String, BigDecimal> summary = sCapitalService.summary(userId);
                BigDecimal shengyu = summary.get("shengyu");
                BigDecimal money = sCapital.getMoney();
                //当前剩余金额 小于要充值的金额，无法进行充值
                if(shengyu.compareTo(money) == -1)
                {
                    return R.error("剩余金额" +
                            "不足，无法进行充值");
                }
                //本帐户出现一条相应的消费记录
                SCapitalEntity entity = new SCapitalEntity();
                entity.setCreateId(userEntity.getUserId());
                entity.setCreateName(userEntity.getUsername());
                entity.setUserId(userEntity.getUserId());
                entity.setUserName(userEntity.getUsername());

                entity.setCreateYear(String.valueOf(calendar.get(Calendar.YEAR)));
                entity.setCreateMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));
                entity.setCreateDay(String.valueOf(calendar.get(Calendar.DATE)));

                entity.setMoney(sCapital.getMoney());
                entity.setMoney(sCapital.getMoney());
                entity.setClientType(sCapital.getClientType());
                entity.setCapitalType(1);

                entity.setRemark("向 账户:"+sCapital.getUserName()+" 充值,"+entity.getMoney() );

                sCapitalService.save(entity);
            }
            else if(sCapital.getCapitalType().equals(1))
            {
                Long yonghuId = sCapital.getUserId();
                HashMap<String, BigDecimal> summary = sCapitalService.summary(yonghuId);
                BigDecimal shengyu = summary.get("shengyu");
                BigDecimal money = sCapital.getMoney();
                //当前剩余金额 小于要充值的金额，无法进行充值
                if(shengyu.compareTo(money) == -1)
                {
                    return R.error("该账户剩余金额不足，请充值");
                }


            }



        }

        sCapitalService.save(sCapital);




        return R.ok();

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:scapital:update")
    public R update(@RequestBody SCapitalEntity sCapital) {
        sCapitalService.updateById(sCapital);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:scapital:delete")
    public R delete(@RequestBody Long[] ids) {
        sCapitalService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 获取当前账号的金额汇总
     */
    @GetMapping("/summary")
    @RequiresPermissions(value={"generator:scapital:list","generator:client:summary"},logical = Logical.OR)
    public R summary() {
        Long userId = ShiroUtils.getUserId();
        HashMap<String, BigDecimal> summary = sCapitalService.summary(userId);
        return R.ok().put("summary", summary);
    }

    /**
     * 获取当前账号的金额汇总，并且携带点击总数和曝光总数
     * @return
     */
    @GetMapping("/summary2")
    @RequiresPermissions("generator:client:summary")
    public R summary2() {
        Long userId = ShiroUtils.getUserId();
        HashMap<String, BigDecimal> summary = sCapitalService.summary(userId);
        HashMap<String, BigDecimal> summary2= sRealDataService.summary(userId);
        summary.putAll(summary2);
        return R.ok().put("summary", summary);
    }

}
