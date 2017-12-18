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
import com.example.utils.Log;

import java.nio.channels.DatagramChannel;
import java.util.TimerTask;

public class BurrowTask extends AbsUDPTask {
    private static final String TAG = "BurrowTask";
    public BurrowTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject reqJson = event.msg;
        JSONObject baseResp = getRespJSONObjectBase(reqJson);
        JSONObject params = reqJson.getJSONObject("params");
        String tag = getStringParam(params, "tag");
        String rtag = getStringParam(params, "rtag");

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
        // if(local.host.equals(remote.host)){
        local.lanBurrowHost = params.getString("lanhost");
        local.lanBurrowPort = params.getIntValue("lanport");
        // }
        BurrowAction burrowAction = new BurrowAction(local, remote);
        final String token = burrowAction.getBurrowToken();
        burrowAction.setActionStep(BurrowAction.Step.CREATE);
        BurrowActionRepository.put(token,burrowAction);
        JSONObject extra = new JSONObject();
        extra.put("token",token);
        baseResp.put("extra",extra);
        stuffAndSendSuccessResp(baseResp,
                event.fromHost,
                event.fromPort);
        Log.e(TAG,"handlePacket: "+baseResp.toString());
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