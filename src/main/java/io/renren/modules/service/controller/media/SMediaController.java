package io.renren.modules.service.controller.media;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.entity.media.SMediaEntity;
import io.renren.modules.service.service.media.SMediaService;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 媒体
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-10 09:44:07
 */
@RestController
@RequestMapping("generator/smedia")
public class SMediaController {
    @Autowired
    private SMediaService sMediaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"generator:smedia:list","generator:client-smedia:list"},logical = Logical.OR)
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sMediaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:smedia:info")
    public R info(@PathVariable("id") Long id){
		SMediaEntity sMedia = sMediaService.getById(id);

        return R.ok().put("sMedia", sMedia);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:smedia:save")
    public R save(@RequestBody SMediaEntity sMedia){

        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        sMedia.setCreateId(userEntity.getUserId());
        sMedia.setCreateName(userEntity.getUsername());
        sMedia.setCreateTime(new Date());

        Calendar calendar = Calendar.getInstance();
        sMedia.setCreateYear(calendar.get(Calendar.YEAR));
        sMedia.setCreateMonth(calendar.get(Calendar.MONTH) + 1);
        sMedia.setCreateDay(calendar.get(Calendar.DATE));

        sMediaService.save(sMedia);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:smedia:update")
    public R update(@RequestBody SMediaEntity sMedia){
		sMediaService.updateById(sMedia);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:smedia:delete")
    public R delete(@RequestBody Long[] ids){
		sMediaService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
