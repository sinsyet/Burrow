package com.example.burrowlib.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowlib.bean.ClientBurrowAction;
import com.example.burrowlib.repository.BurrowActionRepository;
import com.example.jutil.utils.Log;

import java.nio.channels.DatagramChannel;

public class Resp_102Task extends AbsUDPTask {
    private static final String TAG = "Resp_102Task";
    public Resp_102Task(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        Log.e(TAG,"handlePacket: 打洞通啦, 我是主叫 -- "+event.msg.toString()
                +", 对方的主机: "+event.fromHost
                 +", 对方的端口: "+event.fromPort);
        JSONObject msg = event.msg;
        // JSONObject respBase = getRespJSONObjectBase(msg);
        // JSONObject params = msg.getJSONObject("params");
        String token = msg.getString("token");
        // respBase.put("token",token);
        // sendMsg(respBase.toString(),event.fromHost,event.fromPort);
        ClientBurrowAction burrowEvent = BurrowActionRepository.getBurrowEvent(token);
        burrowEvent.updateActiveStamp();
        burrowEvent.updateAction(ClientBurrowAction.ACTION.BURROW_SUCCESS);
    }
}