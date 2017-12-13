package com.example.natclient.fun.impl.channelhandler;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.fun.impl.task.BurrowRespTask;
import com.example.natclient.fun.impl.task.BurrowTask;
import com.example.natclient.fun.impl.task.GetClientTask;
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

public class BaseUDPHandlerImpl extends AbsUDPChannelHandler {
    private static final String TAG = "BaseUDPHandlerImpl";

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private Map<Integer,AbsUDPTask> mTasks = new HashMap<>();
    {
        mTasks.put(-1,new RegisterTask(getChannel()));
        mTasks.put(-2,new PitpatTask(getChannel()));
        mTasks.put(-3,new GetClientTask(getChannel()));
    }

    public BaseUDPHandlerImpl(DatagramChannel channel) {
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
            JSONObject jsonObject = JSONObject.parseObject(msg);
            int t = jsonObject.getIntValue("t");
            jsonObject.put("t",t);
            AbsUDPTask absTask = mTasks.get(t);
            if(absTask != null) {
                absTask.handle(new PacketEvent.Builder()
                        .fromHost(host)
                        .fromPort(port)
                        .msg(jsonObject).build());
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
