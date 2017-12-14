package com.example.burrowserver.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.repository.BurrowActionRepository;
import com.example.burrowserver.server.NatServer;
import com.example.engine.Handler;
import com.example.eventbus.EventBus;
import com.example.eventbus.bean.Event;

import java.nio.channels.DatagramChannel;
import java.util.TimerTask;

/**
 * @author YGX
 *         <p>
 *         <p> 反馈t:-51 的任务</p>
 */
public class Resp_51Task extends AbsUDPTask {

    public Resp_51Task(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        // 获取被叫客户端开放出来的映射端口和外围IP
        String fromHost = event.fromHost;
        int fromPort = event.fromPort;

        // 获取服务器缓存里的打洞对象
        JSONObject msg = event.msg;
        String token = msg.getString("token");
        BurrowAction burrowAction = BurrowActionRepository.getBurrowAction(token);

        // 打洞不存在
        if (burrowAction == null) return;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t", Key.T.REQ_52);
        jsonObject.put("mid", System.currentTimeMillis());
        jsonObject.put("token", token);
        JSONObject params = new JSONObject();
        params.put("rport", fromPort);
        params.put("rhost", fromHost);
        jsonObject.put("params", params);
        NatClient local = burrowAction.getLocal();
        NatClient remote = burrowAction.getRemote();
        remote.burrowPort = fromPort;
        remote.updateActiveStamp();
        local.setTag(jsonObject.toString());
        local.updateActiveStamp();
        burrowAction.setActionStep(BurrowAction.Step.RESP);
        EventBus.post(
                new Event.Builder()
                        .to(NatServer.class)
                        .obj(token)
                        .build());
    }
}