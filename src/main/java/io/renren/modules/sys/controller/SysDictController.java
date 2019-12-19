package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.modules.sys.entity.SysDictEntity;
import io.renren.modules.sys.entity.SysDictItemEntity;
import io.renren.modules.sys.service.SysDictService;
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
 * 系统字典表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:42
 */
@RestController
@RequestMapping("generator/sysdict")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:sysdict:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDictService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:sysdict:info")
    public R info(@PathVariable("id") Long id){
		SysDictEntity sysDict = sysDictService.getById(id);

        return R.ok().put("sysDict", sysDict);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:sysdict:save")
    public R save(@RequestBody SysDictEntity sysDict){
		sysDictService.save(sysDict);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:sysdict:update")
    public R update(@RequestBody SysDictEntity sysDict){
		sysDictService.updateById(sysDict);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:sysdict:delete")
    public R delete(@RequestBody Long[] ids){
		sysDictService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
