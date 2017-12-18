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
    private int actionStep;

    public NatClient getLocal(){ return local;}

    public NatClient getRemote() {
        return remote;
    }

    public int getActionStep(){
        return actionStep;
    }

    public void setActionStep(int step){
        this.actionStep = step;
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

        String host;
        int port;
        if(local.host.equals(remote.host)){
            extra.put("rtype",1);
            host = local.lanBurrowHost;
            port = local.lanBurrowPort;
        }else {
            extra.put("rtype",0);
            host = local.host;
            port = local.burrowPort;
        }
        extra.put("host",host);
        extra.put("ltag",local.tag);
        extra.put("port",port);
        jsonObject.put("params",extra);

        channel.send(
                ByteBuffer.wrap(jsonObject.toString().getBytes("UTF-8")),
                new InetSocketAddress(remote.host,remote.port));

    }

    public interface Step{
        int CREATE      = 51;
        int RESP        = 52;
    }
}
