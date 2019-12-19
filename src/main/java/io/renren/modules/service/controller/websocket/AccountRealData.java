package io.renren.modules.service.controller.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.modules.service.service.capital.SCapitalService;
import io.renren.modules.service.service.realdata.SRealDataService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint("/websocket/account_basic_websocket/{cid}")
@Component
public class AccountRealData {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<AccountRealData> webSocketSet = new CopyOnWriteArraySet<AccountRealData>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 接收cid
    private String cid = "";

    //此处是解决无法注入的关键
    private static ApplicationContext applicationContext;

    //要注入的service或者dao
    public static void setApplicationContext(ApplicationContext applicationContext) {
        AccountRealData.applicationContext = applicationContext;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("cid") String cid) {
        if (!webSocketSet.contains(this)) {
            this.session = session;
            webSocketSet.add(this);     // 加入set中
            addOnlineCount();           // 在线数加1
            System.out.print("AccountRealData 客户端: " + cid + " 连接成功, 当前在线人数为：" + getOnlineCount());
            this.cid = cid;
            try {
                sendMessage("连接成功");
            } catch (IOException e) {
                System.out.print("AccountRealData 发送消息异常：" + e.toString());
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  // 从set中删除
        subOnlineCount();           // 在线数减1
        System.out.print("AccountRealData 有一个连接关闭，当前在线人数为：" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        System.out.print("AccountRealData 收到来自客户端 " + cid + " 的信息: " + message);

        SRealDataService sRealDataService = applicationContext.getBean(SRealDataService.class);
        SCapitalService sCapitalService = applicationContext.getBean(SCapitalService.class);
        if (message.equals("get account message")) {
            HashMap<String, BigDecimal> summary = sCapitalService.summary(Long.valueOf(cid));
            try {
                sendInfo(new ObjectMapper().writeValueAsString(summary), cid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(message.equals("close"))
        {
            try {
                this.session.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.print("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("cid") String cid) throws IOException {
        System.out.print("AccountRealData 推送消息到客户端：" + cid + "，内容: " + message);
        for (AccountRealData item : webSocketSet) {
            try {
                // 这里可以设定只推送给这个cid的，为null则全部推送
                if (cid == null) {
                    item.sendMessage(message);
                } else if (item.cid.equals(cid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        AccountRealData.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        AccountRealData.onlineCount--;
    }
}
