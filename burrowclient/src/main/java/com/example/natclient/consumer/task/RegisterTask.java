package com.example.natclient.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.eventbus.EventBus;
import com.example.eventbus.bean.Event;
import com.example.natclient.NatClient;
import com.example.natclient.app.Key;
import com.example.natclient.bean.NatResponse;
import com.example.natclient.engine.RequestQueue;
import com.example.natclient.fun.base.IRequestObserver;

import java.nio.channels.DatagramChannel;


/**
 *
 * @author YGX
 *
 * 注册任务
 */

public class RegisterTask extends AbsUDPTask {

    public RegisterTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject json = event.msg;
        long mid = json.getLongValue("mid");
        int code = json.getIntValue("code");
        IRequestObserver observer = RequestQueue.remove(mid);
        NatResponse resp = null;

        if(code == 200){
            JSONObject result = json.getJSONObject("extra");
            String tag = result.getString("tag");
            if(observer != null){
                resp = new NatResponse.Builder()
                        .success(true)
                        .r(result.toString())
                        .code(Key.Code.OK)
                        .build();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("host",event.fromHost);
                jsonObject.put("port",event.fromPort);
                jsonObject.put("tag",tag);
                EventBus.post(new Event.Builder()
                        .to(NatClient.class)
                        .obj(jsonObject)
                        .build());
            }
        }else {
            if(observer != null){
                resp = new NatResponse.Builder()
                        .success(true)
                        .em(json.getString("em"))
                        .code(code)
                        .build();
            }
        }
        if(observer != null){
            observer.onResponse(resp);
        }
    }
}
