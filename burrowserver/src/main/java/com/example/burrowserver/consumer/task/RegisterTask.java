package com.example.burrowserver.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.repository.NatClientRepository;
import com.example.utils.NatUtil;

import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 *
 * <p> 客户端注册 </p>
 */
public class RegisterTask extends AbsUDPTask {

    public RegisterTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject respJSONObjectBase = getRespJSONObjectBase(event.msg);
        if(respJSONObjectBase == null) return;

        String tag = NatUtil.generateTag(event.fromHost, event.fromPort);
        JSONObject extra = new JSONObject();
        extra.put("tag",tag);
        respJSONObjectBase.put("extra",extra);
        if(!NatClientRepository.containsTag(tag)){
            NatClientRepository.put(tag,
                    new NatClient(
                            event.fromHost,
                            event.fromPort,
                            System.currentTimeMillis(),
                            tag));
        }
        stuffAndSendSuccessResp(respJSONObjectBase,event.fromHost,event.fromPort);
    }
}