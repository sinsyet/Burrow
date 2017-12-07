package com.example.burrowserver.engine.base;

import com.example.burrowserver.engine.fun.IAcceptMsgHandler;
import com.example.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author ygx
 *
 * 消息处理基类
 */

public abstract class AbsAcceptMsgHandler implements IAcceptMsgHandler {
    private static final String TAG = AbsAcceptMsgHandler.class.getSimpleName();
    protected DatagramChannel channel;


    public AbsAcceptMsgHandler(DatagramChannel channel){
        if(channel == null) throw new IllegalArgumentException("channel can't be null");
        this.channel = channel;
    }

    protected DatagramChannel getChannel(){
        return channel;
    }

    protected synchronized void sendMsg(String msg,String host,int port){
        if(msg == null)
            throw new IllegalArgumentException("msg can't be null");

        InetSocketAddress target = new InetSocketAddress(host, port);
        try {
            channel.send(ByteBuffer.wrap(msg.getBytes("UTF-8")),target);
        } catch (IOException e) {
            Log.e(TAG,"send msg fail ",e);
        }
    }
}
