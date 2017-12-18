package com.example.natclient.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;

import java.nio.channels.DatagramChannel;

public class EchoTask extends AbsUDPTask {
    public EchoTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        int lanPort = event.fromPort;
        String lanHost = event.fromHost;
        JSONObject jsonObject = event.msg;
        JSONObject extra = new JSONObject();
        extra.put("lanport",lanPort);
        extra.put("lanhost",lanHost);
        JSONObject respBase = getRespJSONObjectBase(jsonObject);
        respBase.put("extra",extra);
        stuffAndSendSuccessResp(respBase,lanHost,lanPort);
    }
}