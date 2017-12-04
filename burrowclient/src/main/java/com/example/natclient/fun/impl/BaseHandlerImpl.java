package com.example.natclient.fun.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.utils.Log;
import com.example.natclient.utils.NatUtil;

import java.io.IOException;
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

public class BaseHandlerImpl extends AbsSelectedHandler{
    private static final String TAG = "BaseHandlerImpl";

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private Map<Integer,AbsTask> mTasks = new HashMap<>();
    {
        mTasks.put(-1,new RegisterTask(this));
        mTasks.put(-2,new PitpatTask(this));
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
}
