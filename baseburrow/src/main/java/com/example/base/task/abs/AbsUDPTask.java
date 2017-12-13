package com.example.base.task.abs;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.ifun.ITask;
import com.example.engine.TaskExecutors;
import com.example.utils.Log;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public abstract class AbsUDPTask implements ITask<PacketEvent> {
    private static final String TAG = "AbsUDPTask";
    private DatagramChannel channel;
    private PacketEvent event;
    public AbsUDPTask(DatagramChannel channel){
        this.channel = channel;
    }

    @Override
    public void handle(PacketEvent event) {
        this.event = event;
        TaskExecutors.exec(r);
    }

    protected Runnable r = () -> {
        synchronized (AbsUDPTask.this) {
            final PacketEvent temp = event;
            event = null;
            try {
                handlePacket(temp);
            }catch (Exception e){
                Log.e(TAG,"handlePacket Exception: "+e.getMessage());
            }
        }
    };

    protected JSONObject getRespJSONObjectBase(JSONObject json){
        JSONObject resp = new JSONObject();
        if (!json.containsKey("t")) {
            return null;
        }

        int t = json.getIntValue("t");
        resp.put("t",- t);
        if (json.containsKey("mid")) {
            resp.put("mid",json.getLongValue("mid"));
        }
        return resp;
    }

    protected synchronized boolean sendMsg(String msg,
                                           String toHost,
                                           int toPort){
        if(msg == null
                || toHost == null
                || (toPort < 1 || toPort > 65535)){
            throw new IllegalStateException("parameters invalid: "
                    + msg + " "
                    + toHost+" "
                    + toPort);
        }
        try{
            channel.send(ByteBuffer.wrap(msg.getBytes("UTF-8")),
                    new InetSocketAddress(toHost,toPort));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    protected boolean stuffAndSendSuccessResp(JSONObject resp,
                                              String toHost,
                                              int toPort){
        resp.put("code", Key.Code.OK);
        return sendMsg(resp.toString(),toHost,toPort);
    }

    protected JSONObject stuffFailureResp(JSONObject resp,int code,String em){
        resp.put("code",code);
        resp.put("em",em);
        return resp;
    }

    protected abstract void handlePacket(PacketEvent event);
}