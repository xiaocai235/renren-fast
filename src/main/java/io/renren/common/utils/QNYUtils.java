package io.renren.common.utils;

import com.qiniu.util.Auth;
import io.renren.modules.oss.cloud.CloudStorageConfig;
import io.renren.modules.sys.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QNYUtils {

    @Autowired
    private SysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
    //将QINIUYUN 公有链接进行授权
    public String privateUrl2publicUrl(String url)
    {
        if(url != null && StringUtils.isNotEmpty(url) &&!url.contains("token"))
        {
            CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

            Auth auth = Auth.create(config.getQiniuAccessKey(),config.getQiniuSecretKey());
            long expireInSeconds = 3600*24*365*5;//伪永久性token
            String finalUrl = auth.privateDownloadUrl(url, expireInSeconds);
            return finalUrl;
        }
        return "";
    }
}
