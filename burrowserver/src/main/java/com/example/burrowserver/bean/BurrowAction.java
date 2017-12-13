package com.example.burrowserver.bean;

import com.alibaba.fastjson.JSONObject;
import com.example.base.key.Key;
import com.example.burrowserver.engine.repository.Repository;
import com.example.utils.NatUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class BurrowAction {
    private NatClient local;
    private NatClient remote;
    private long activeStamp;

    public NatClient getLocal(){ return local;}

    public NatClient getRemote() {
        return remote;
    }

    public BurrowAction(NatClient local, NatClient remote){
        this.local = local;
        this.remote = remote;
    }


    /**
     * 生成打洞token
     * @return 打洞token
     */
    public String getBurrowToken(){
        return NatUtil.base64Encode(local.host,local.burrowPort,
                remote.host);
    }

    public void launch(DatagramChannel channel) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t", Key.T.REQ_51);
        long mid = System.currentTimeMillis();
        jsonObject.put("mid", mid);
        jsonObject.put("token",getBurrowToken());
        JSONObject extra = new JSONObject();
        extra.put("host",local.host);
        extra.put("ltag",local.tag);
        extra.put("port",local.burrowPort);
        jsonObject.put("extra",extra);

        channel.send(
                ByteBuffer.wrap(jsonObject.toString().getBytes("UTF-8")),
                new InetSocketAddress(remote.host,remote.port));

    }
}
