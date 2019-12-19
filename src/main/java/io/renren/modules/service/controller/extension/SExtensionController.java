package io.renren.modules.service.controller.extension;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.service.entity.extension.SExtensionEntity;
import io.renren.modules.service.service.extension.SExtensionService;
import io.renren.modules.sys.entity.SysUserEntity;
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
 * 推广页面表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 17:25:40
 */
@RestController
@RequestMapping("generator/sextension")
public class SExtensionController {
    @Autowired
    private SExtensionService sExtensionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:sextension:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sExtensionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:sextension:info")
    public R info(@PathVariable("id") Long id){
		SExtensionEntity sExtension = sExtensionService.getById(id);

        return R.ok().put("sExtension", sExtension);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:sextension:save")
    public R save(@RequestBody SExtensionEntity sExtension){

        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        sExtension.setCreateId(sysUserEntity.getUserId());
        sExtension.setCreateName(sysUserEntity.getUsername());

        sExtensionService.save(sExtension);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:sextension:update")
    public R update(@RequestBody SExtensionEntity sExtension){
		sExtensionService.updateById(sExtension);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:sextension:delete")
    public R delete(@RequestBody Long[] ids){
		sExtensionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
