package com.example.burrowserver.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.repository.BurrowActionRepository;
import com.example.burrowserver.engine.repository.NatClientRepository;
import com.example.burrowserver.server.NatServer;
import com.example.engine.Handler;
import com.example.eventbus.EventBus;
import com.example.eventbus.bean.Event;

import java.nio.channels.DatagramChannel;
import java.util.TimerTask;

public class BurrowTask extends AbsUDPTask {
    public BurrowTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject reqJson = event.msg;
        JSONObject baseResp = getRespJSONObjectBase(reqJson);
        String tag = getStringParam(reqJson, "tag");
        String rtag = getStringParam(reqJson, "rtag");

        if (!NatClientRepository.containsTag(tag)) {
            stuffFailureResp(baseResp, Key.Code.NO_LTAG,Key.Em.NO_LTAG);
            sendMsg(baseResp.toString(),event.fromHost,event.fromPort);
            return;
        }
        if (!NatClientRepository.containsTag(rtag)) {
            stuffFailureResp(baseResp, Key.Code.NO_RTAG,Key.Em.NO_RTAG);
            sendMsg(baseResp.toString(),event.fromHost,event.fromPort);
            return;
        }

        NatClient local = NatClientRepository.getNatClient(tag);
        local.burrowPort = event.fromPort;
        NatClient remote = NatClientRepository.getNatClient(rtag);

        BurrowAction burrowAction = new BurrowAction(local, remote);
        final String token = burrowAction.getBurrowToken();
        BurrowActionRepository.put(token,burrowAction);
        JSONObject extra = new JSONObject();
        extra.put("token",token);
        baseResp.put("extra",extra);
        stuffAndSendSuccessResp(baseResp,
                event.fromHost,
                event.fromPort);
        Handler.post(new TimerTask() {
            @Override
            public void run() {
                EventBus.post(new Event.Builder()
                    .to(NatServer.class)
                    .obj(token)
                    .build()
                );
            }
        },100);
    }

    private String getStringParam(JSONObject reqJson,String key){
        if(reqJson == null) return null;
        return reqJson.getString(key);
    }
}