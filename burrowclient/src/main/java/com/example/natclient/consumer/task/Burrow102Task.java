package com.example.natclient.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;

import java.nio.channels.DatagramChannel;

public class Burrow102Task extends AbsUDPTask {

    public Burrow102Task(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject msg = event.msg;
        JSONObject respBase = getRespJSONObjectBase(msg);
        JSONObject params = msg.getJSONObject("params");
        String rhost = params.getString("rhost");
        int rport = params.getIntValue("rport");
        String token = msg.getString("token");
        respBase.put("token", token);
        // response to server
        sendMsg(respBase.toString(),event.fromHost,event.fromPort);

        // request to clientB
        JSONObject req = new JSONObject();
        req.put("t", Key.T.REQ_102);
        req.put("mid",System.currentTimeMillis());
        req.put("token",token);
        sendMsg(req.toString(),rhost,rport);
    }
}