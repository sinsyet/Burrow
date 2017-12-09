package com.example.natclient.fun.impl.task;

import com.alibaba.fastjson.JSONObject;
import com.example.engine.Handler;
import com.example.natclient.app.Key;
import com.example.natclient.bean.BurrowEvent;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.repository.Repository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.TimerTask;

/**
 * @author YGX
 *
 * 接收到打洞请求时执行的任务,
 *
 * 处理逻辑就是将准备打洞使用的端口返回给服务器,
 * 注意, 只返回端口, ip或主机由服务器去获取, 因为客户端获取到的只是局域网的ip;
 * 服务器才能获取到公网ip;
 */
public class BurrowTask extends AbsTask {
    private final DatagramChannel channel;

    public BurrowTask(AbsSelectedHandler handler, DatagramChannel channel) {
        super(handler);
        this.channel = channel;
    }

    @Override
    protected void onRunTask(JSONObject json) {
        JSONObject respJson = getRespJson(json);
        String token = json.getString("token");
        BurrowEvent burrowAction = Repository.get(token);
        if (burrowAction == null) {
            burrowAction = new BurrowEvent();
            JSONObject extra = json.getJSONObject("extra");
            burrowAction.host = extra.getString("host");
            burrowAction.port = extra.getIntValue("port");
            burrowAction.token = token;
            Repository.put(token,burrowAction);
        }
        respJson.put("code", Key.Code.OK);
        respJson.put("token",token);
        sendMsg(respJson.toString(),json.getString("host"),json.getIntValue("port"));
        final BurrowEvent action = burrowAction;
        Handler.post(new TimerTask() {
            @Override
            public void run() {
                JSONObject sayHi = new JSONObject();
                sayHi.put("t",22);
                sayHi.put("mid",System.currentTimeMillis());
                JSONObject extra2 = new JSONObject();
                extra2.put("msg","Hi, zhujiao");
                sayHi.put("extra",extra2);
                sendMsg(sayHi.toString(),action.host,action.port);
            }
        },500);
    }

    @Override
    protected void sendMsg(String msg, String host, int port) {
        try {
            channel.send(ByteBuffer.wrap(msg.getBytes("UTF-8")),new InetSocketAddress(host,port));
        } catch (IOException e) {
        }
    }
}