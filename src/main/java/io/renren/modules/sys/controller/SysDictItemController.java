package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.sys.entity.SysDictItemEntity;
import io.renren.modules.sys.service.SysDictItemService;
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
 * 系统字典项表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:41
 */
@RestController
@RequestMapping("generator/sysdictitem")
public class SysDictItemController {
    @Autowired
    private SysDictItemService sysDictItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:sysdictitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDictItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:sysdictitem:info")
    public R info(@PathVariable("id") Long id){
		SysDictItemEntity sysDictItem = sysDictItemService.getById(id);

        return R.ok().put("sysDictItem", sysDictItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:sysdictitem:save")
    public R save(@RequestBody SysDictItemEntity sysDictItem){
		sysDictItemService.save(sysDictItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:sysdictitem:update")
    public R update(@RequestBody SysDictItemEntity sysDictItem){
		sysDictItemService.updateById(sysDictItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:sysdictitem:delete")
    public R delete(@RequestBody Long[] ids){
		sysDictItemService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/name")
    public R getDictItemListByDictName(@RequestParam String dictName)
    {
        List<SysDictItemEntity> list = sysDictItemService.getDictItemListByDictName(dictName);
        return R.ok().put("list",list);
    }
}
