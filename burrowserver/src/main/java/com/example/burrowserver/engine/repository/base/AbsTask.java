package com.example.burrowserver.engine.repository.base;


import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.engine.TaskExecutor;
import com.example.utils.Log;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public abstract class AbsTask {
    private static final String TAG = AbsTask.class.getSimpleName();
    private DatagramChannel channel;
    protected JSONObject obj;

    public AbsTask(DatagramChannel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel can't be null");
        }
        this.channel = channel;
    }
    private String host;
    private int port;
    private Runnable r = new Runnable() {

        @Override
        public void run() {
            host = obj.getString("host");
            port = obj.getIntValue("port");

            onRunTask(obj);
        }
    };


    public void runTask(JSONObject obj) {
        this.obj = obj;
        TaskExecutor.exec(r);
    }

    protected void sendMsg(String msg, String host, int port) {
        if (msg == null) throw new IllegalArgumentException("send msg can't be null");
        try {
            InetSocketAddress target = new InetSocketAddress(host, port);
            channel.send(ByteBuffer.wrap(msg.getBytes("UTF-8")), target);
        } catch (Exception e) {
            Log.e(TAG, "send msg error", e);
        }
    }

    protected void sendMsg(String msg){
        sendMsg(msg,host,port);
    }

    protected JSONObject getResponseJson(JSONObject obj){
        JSONObject resp = new JSONObject();
        resp.put("t",-obj.getIntValue("t"));
        if(obj.containsKey("mid")) {
            resp.put("mid", obj.getLongValue("mid"));
        }
        return resp;
    }
    /*protected abstract boolean reqSuccess(JSONObject obj);
    protected abstract */
    protected abstract void onRunTask(JSONObject obj);
}
