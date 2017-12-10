package com.example.natclient.fun.base;

import com.alibaba.fastjson.JSONObject;
import com.example.engine.TaskExecutors;
import com.example.eventbus.EventBus;
import com.example.eventbus.bean.Event;
import com.example.natclient.NatClient;
import com.example.natclient.bean.Message;

/**
 *
 * @author YGX
 *
 * 任务
 */

public abstract class AbsTask {

    protected AbsUDPChannelHandler handler;
    private JSONObject json;

    public AbsTask(AbsUDPChannelHandler handler){
        this.handler = handler;
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                onRunTask(json);
            }catch (Exception ignored){

            }
        }
    };

    public void runTask(JSONObject json){
        this.json = json;
        TaskExecutors.exec(r);
    }

    protected JSONObject getRespJson(JSONObject obj){
        JSONObject resp = new JSONObject();
        if(obj.containsKey("t")) {
            int t = obj.getIntValue("t");
            resp.put("t",-t);
        }
        if(obj.containsKey("mid")) {
            long mid = obj.getLongValue("mid");
            resp.put("mid",mid);
        }
        return resp;
    }

    protected void sendMsg(String msg,String host,int port){
        Message message = new Message();
        message.msg = msg;
        message.host = host;
        message.port = port;
        EventBus.post(
                new Event.Builder()
                        .obj(message)
                        .to(NatClient.class)
                        .build()
        );
    }
    protected abstract void onRunTask(JSONObject json);
}
