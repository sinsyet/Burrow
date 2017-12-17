package com.example.burrowlib.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowlib.bean.ClientBurrowAction;
import com.example.burrowlib.repository.BurrowActionRepository;

import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 *
 * <p> 接受服务器通告的任务 </p>
 *
 * <pre>
 *     52是服务器主动通知客户端的消息
 * </pre>
 */
public class Resp52Task extends AbsUDPTask {

    public Resp52Task(DatagramChannel channel) {
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

        ClientBurrowAction clientBurrowAction = new ClientBurrowAction(
                rhost,
                rport,
                token,
                event.fromHost,
                event.fromPort,
                ClientBurrowAction.BurrowRole.ACTIVE);
        BurrowActionRepository.put(token,clientBurrowAction);
        // request to clientB
        JSONObject req = new JSONObject();
        req.put("t", Key.T.REQ_102);
        req.put("mid",System.currentTimeMillis());
        req.put("token",token);
        sendMsg(req.toString(),rhost,rport);
    }
}