package com.example.natclient.consumer.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.eventbus.EventBus;
import com.example.eventbus.bean.Event;
import com.example.natclient.NatClient;
import com.example.natclient.bean.ClientBurrowAction;
import com.example.natclient.repository.BurrowActionRepository;
import com.example.utils.Log;
import com.example.utils.NatUtil;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 *
 * <p> 作为被叫接收到打洞请求的时候 </p>
 *
 * 处理逻辑就是使用准备打洞使用的udp channel发送数据给服务端,
 * 以便使服务端获取到被叫用于打洞的channel的公网ip和映射端口
 */
public class BurrowTask extends AbsUDPTask {
    private static final String TAG = "BurrowTask";
public BurrowTask(DatagramChannel channel) {
    super(channel);
}

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject msg = event.msg;
        JSONObject params = msg.getJSONObject("params");
        String host = getStringParam(params, "host");
        String token = msg.getString("token");
        int port = getIntParam(params, "port");

        int rType = getIntParam(params, "rtype");
        ClientBurrowAction burrowEvent = new ClientBurrowAction(
                host,
                port,
                token,
                event.fromHost,
                event.fromPort,
                ClientBurrowAction.BurrowRole.PASSIVE,
                rType);
        JSONObject respJson = getRespJSONObjectBase(msg);
        respJson.put("token",token);
        respJson.put("code", Key.Code.OK);

        DatagramSocket socket = getChannel().socket();

        int lanPort = socket.getLocalPort();
        String lanHost = NatUtil.getLocalAddress();

        Log.e(TAG, "handlePacket: "+respJson.toString());
        burrowEvent.setTag(respJson.toString());
        BurrowActionRepository.put(burrowEvent.getToken(),burrowEvent);
        EventBus.post(
                new Event.Builder()
                        .to(NatClient.class)
                        .obj(burrowEvent.getToken())
                        .build()
        );
    }

    private String getStringParam(JSONObject param, String key){
        return param.getString(key);
    }

    private int getIntParam(JSONObject param, String key){
        return param.getIntValue(key);
    }
}