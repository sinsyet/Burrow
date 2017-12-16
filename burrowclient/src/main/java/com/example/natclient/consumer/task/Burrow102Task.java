package com.example.natclient.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.natclient.bean.ClientBurrowAction;
import com.example.natclient.repository.BurrowActionRepository;
import com.example.utils.Log;

import java.nio.channels.DatagramChannel;

public class Burrow102Task extends AbsUDPTask {
    private static final String TAG = "Burrow102Task";
    public Burrow102Task(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject msg = event.msg;
        String fromHost = event.fromHost;
        int fromPort = event.fromPort;

        JSONObject respBase = getRespJSONObjectBase(msg);
        String token = msg.getString("token");
        respBase.put("token",token);
        ClientBurrowAction burrowEvent =
                BurrowActionRepository.getBurrowEvent(token);
        burrowEvent.updateActiveStamp();
        burrowEvent.updateAction(ClientBurrowAction.ACTION.CONNECT);
        Log.e(TAG, "handlePacket: 打洞通啦 --我是被叫 -- fromHost: "
                + fromHost
                +" fromPort: "+fromPort
                +" msg: "+msg);

        sendMsg(respBase.toString(),fromHost,fromPort);
    }
}