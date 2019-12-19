package io.renren.modules.service.controller.material;

import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.system.SystemUtil;
import io.renren.common.utils.*;
import io.renren.modules.oss.cloud.CloudStorageConfig;
import io.renren.modules.service.entity.material.SMaterialEntity;
import io.renren.modules.service.service.material.SMaterialService;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


/**
 * 文件素材表，文件并不是真正存储到服务器当中，而是将文件上传到文件配置的云存储中
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-02 15:22:56
 */
@RestController
@RequestMapping("generator/smaterial")
public class SMaterialController {
    @Autowired
    private SMaterialService sMaterialService;

    @Autowired
    private QNYUtils qnyUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions(value={"generator:smaterial:list","generator:client-smaterial:list"},logical = Logical.OR)
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = sMaterialService.queryPage(params);

        List<SMaterialEntity> list = (List<SMaterialEntity>) page.getList();
        for(SMaterialEntity item:list)
        {
            String fileUrl = item.getFileUrl();
            splitFileUrls(fileUrl,item);
        }


        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:smaterial:info")
    public R info(@PathVariable("id") Long id){
		SMaterialEntity sMaterial = sMaterialService.getById(id);
        String fileUrl = sMaterial.getFileUrl();
        splitFileUrls(fileUrl,sMaterial);
        return R.ok().put("sMaterial", sMaterial);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:smaterial:save")
    public R save(@RequestBody SMaterialEntity sMaterial){

        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        sMaterial.setCreateId(sysUserEntity.getUserId());
        sMaterial.setCreateName(sysUserEntity.getUsername());
        sMaterialService.save(sMaterial);
        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:smaterial:update")
    public R update(@RequestBody SMaterialEntity sMaterial){
		sMaterialService.updateById(sMaterial);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:smaterial:delete")
    public R delete(@RequestBody Long[] ids){
		sMaterialService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    private void splitFileUrls(String fileUrl,SMaterialEntity bean)
    {
        bean.setUrls(new ArrayList<String>());
        if(!StringUtils.isEmpty(fileUrl))
        {
            //多张图片
            if(fileUrl.contains("-item-"))
            {
                String[] subitems = fileUrl.split("-item-");
                for(String subitem: subitems)
                {
                    String[] split = subitem.split("-id-");

                    bean.getUrls().add(qnyUtils.privateUrl2publicUrl(split[1]));
                }

            }
            //单张图片
            else
            {
                String[] split = fileUrl.split("-id-");
                bean.getUrls().add(qnyUtils.privateUrl2publicUrl(split[1]));
            }
        }

    }

}
