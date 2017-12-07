package com.example.natclient.fun.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.engine.Handler;
import com.example.eventbus.EventBus;
import com.example.natclient.app.Key;
import com.example.natclient.bean.BurrowAction;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.repository.Repository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.TimerTask;

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
        BurrowAction burrowAction = Repository.get(token);
        if (burrowAction == null) {
            burrowAction = new BurrowAction();
            JSONObject extra = json.getJSONObject("extra");
            burrowAction.host = extra.getString("host");
            burrowAction.port = extra.getIntValue("port");
            burrowAction.token = token;
            Repository.put(token,burrowAction);
        }
        respJson.put("code", Key.Code.OK);
        respJson.put("token",token);
        sendMsg(respJson.toString(),json.getString("host"),json.getIntValue("port"));
        final BurrowAction action = burrowAction;
        Handler.post(new TimerTask() {
            @Override
            public void run() {
                JSONObject sayHi = new JSONObject();
                sayHi.put("t",21);
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