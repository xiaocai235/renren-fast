package io.renren.common.utils;

import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Component
public class WeBankUtils {

    public final String DEF_CHATSET = "UTF-8";
    public final int DEF_CONN_TIMEOUT = 30000;
    public final int DEF_READ_TIMEOUT = 30000;
    public final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public final String URL = "http://47.110.184.28:8081";
    public final String USERID = "351133";
    public final String APIKEY = "92cb7e4905f148ab8eef7416b6c3c885";


    /**
     * 发送短信
     * @param phones	不能为空，手机号码，多手机号用逗号分隔如 13300000000,13200000000
     * @param content	不能为空，短信内容(内容前面必须要带上签名如:【宽信科技】)
     */
    public HashMap<String,String> smsSend(String phones, String content) throws Exception {
        HashMap<String, String> result = new HashMap<>();
        ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com", "101665", "beb8dbb1-ab38-468e-a037-d9c12e1eec7b");
        String send = client.send(phones, content);
        result.put("result",send);
        return result;
    }

}
