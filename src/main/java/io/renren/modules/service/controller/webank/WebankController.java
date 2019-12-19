package io.renren.modules.service.controller.webank;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.renren.common.utils.*;
import io.renren.modules.service.entity.webank.WebankEntity;
import io.renren.modules.service.service.webank.WebankService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-10-25 12:38:10
 */
@RestController
@RequestMapping("generator/webank")
public class WebankController {
    @Autowired
    private WebankService webankService;

    @Autowired
    private WeBankUtils weBankUtils;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("/sendCode")
    public R sendCode(@RequestBody WebankEntity webank, HttpServletRequest request) throws Exception {
        String phone = webank.getPhone();
        String code = getRandNum(6);

        String content = "您的短信验证码为："+code;
        HashMap<String, String> map = weBankUtils.smsSend(phone, content);
        String result = map.get("result");
        if(result.contains("发送成功"))
        {
            request.getSession().setAttribute(phone,code);
            ScheduledExecutorService mService = new ScheduledThreadPoolExecutor(20,
                    new BasicThreadFactory.Builder().namingPattern("all-schedule-pool-%d").daemon(true).build());
            mService.schedule(new Runnable() {
                @Override
                public void run() {
                    request.getSession().removeAttribute(phone);
                    System.out.println("从 session 中删除 "+phone+" 短信验证码 "+code);
                }

            }, 180, TimeUnit.SECONDS);
            redisUtils.set("code:"+phone,code,180);
        }
        else
        {
            return R.error(result);
        }

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webankService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		WebankEntity webank = webankService.getById(id);

        return R.ok().put("webank", webank);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WebankEntity webank, HttpServletRequest request){

        Object o = request.getSession().getAttribute(webank.getPhone());
        if(o == null)
        {
            return R.error("短信验证码不存在或过期,请重新发送");
        }

        if(!String.valueOf(o).equals(webank.getCode()))
        {
            return R.error("短信验证码错误");
        }
        else
        {
            String ip = getIPAddress(request);
            webank.setIp(ip);
            webank.setCreateTime(new Date());
            webankService.save(webank);
            return R.ok();
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebankEntity webank){
		webankService.updateById(webank);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		webankService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/mo")
    public String mo(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        System.out.println(writer.toString());
        return "success";
    }
    @RequestMapping("/rollback")
    public String rollback(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        System.out.println(writer.toString());
        return "success";
    }


    public String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

    public String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    public int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
}
