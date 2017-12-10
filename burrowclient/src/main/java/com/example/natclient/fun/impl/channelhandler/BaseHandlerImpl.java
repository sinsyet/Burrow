package com.example.natclient.fun.impl.channelhandler;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.fun.impl.task.BurrowRespTask;
import com.example.natclient.fun.impl.task.BurrowTask;
import com.example.natclient.fun.impl.task.PitpatTask;
import com.example.natclient.fun.impl.task.RegisterTask;
import com.example.utils.Log;
import com.example.utils.NatUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author YGX
 *
 * 基本处理类
 */

public class BaseHandlerImpl extends AbsUDPChannelHandler {
    private static final String TAG = "BaseHandlerImpl";

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private Map<Integer,AbsTask> mTasks = new HashMap<>();
    {
        mTasks.put(-1,new RegisterTask(this));
        mTasks.put(-2,new PitpatTask(this));
        mTasks.put(10,new BurrowTask(this,getChannel()));
        mTasks.put(12,new BurrowRespTask(this));
    }

    public BaseHandlerImpl(DatagramChannel channel) {
        super(channel);
    }

    @Override
    public void onRead(SelectionKey key) {
        DatagramChannel channel = (DatagramChannel) key.channel();
        try {
            InetSocketAddress server = (InetSocketAddress) channel.receive(byteBuffer);
            String host = server.getAddress().getHostAddress();
            int port = server.getPort();
            String msg = NatUtil.getMsgByByteBuffer(byteBuffer, true);
            Log.e(TAG,"receive msg: "+msg);
            JSONObject jsonObject = JSONObject.parseObject(msg);
            jsonObject.put("host",host);
            jsonObject.put("port",port);
            int t = jsonObject.getIntValue("t");
            jsonObject.put("t",t);
            AbsTask absTask = mTasks.get(t);
            if(absTask != null) {
                absTask.runTask(jsonObject);
            }else {
                throw new NullPointerException(" don't support this type: "+t);
            }
        } catch (Exception e) {
            Log.e(TAG,"error when handle accept: "+e.getMessage());
        }
    }

    @Override
    public void onAccept(SelectionKey key) {

    }

    @Override
    public void onWrite(SelectionKey key) {

    }

    @Override
    public void onConnect(SelectionKey key) {

    }
}
