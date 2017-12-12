package com.example.burrowserver.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.engine.repository.NatClientRepository;

import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 *
 * <p> 客户端发送心跳 </p>
 */
public class PitpatTask extends AbsUDPTask {
    public PitpatTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject respJSONObjectBase = getRespJSONObjectBase(event.msg);
        String tag = getTag(event.msg);
        if(!NatClientRepository.containsTag(tag)){
            return;
        }
        NatClientRepository.updateActiveStamp(tag);
        sendMsg(respJSONObjectBase.toString(),event.fromHost,event.fromPort);
    }

    private String getTag(JSONObject object){
        return object.getString("tag");
    }
}