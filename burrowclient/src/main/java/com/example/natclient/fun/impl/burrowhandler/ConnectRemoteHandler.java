package com.example.natclient.fun.impl.burrowhandler;

import com.alibaba.fastjson.JSONObject;
import com.example.eventbus.EventBus;
import com.example.eventbus.bean.Event;
import com.example.natclient.NatClient;
import com.example.natclient.bean.BurrowEvent;
import com.example.natclient.bean.Message;
import com.example.natclient.fun.base.AbsBurrowHandler;

/**
 * @author YGX
 *
 * 连接远程
 */
public class ConnectRemoteHandler extends AbsBurrowHandler {
    @Override
    protected void handleBurrow(BurrowEvent action) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t",21);
        jsonObject.put("token",action.token);
        EventBus.post(new Event
                .Builder()
                .to(NatClient.class)
                .obj(
                        new Message(
                                action.host,
                                action.port,
                                jsonObject.toString())
                ).build()
        );
    }

    @Override
    protected boolean dispatchBurrowAction(BurrowEvent action) {
        return action.action == BurrowEvent.ACTION.CONNECT;
    }
}