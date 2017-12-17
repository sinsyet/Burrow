package com.example.burrowlib.consumer.channel;

import com.alibaba.fastjson.JSONObject;
import com.example.base.channel.abs.AbsUDPChannelHandler;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.event.PacketEvent;
import com.example.jutil.utils.Log;
import com.example.jutil.utils.NatUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

/**
 * @author YGX
 *
 * <p> udp协议通道处理器实现类 </p>
 */
public class BaseUDPChannelHandlerImpl extends AbsUDPChannelHandler {
    private static final String TAG = "BaseUDPChannelHandlerImpl";
    private ByteBuffer buf = ByteBuffer.allocate(1024);
    public BaseUDPChannelHandlerImpl(AbsPacketConsumer consumer) {
        super(consumer);
    }

    @Override
    public void onRead(SelectionKey key) {
        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            InetSocketAddress receive = (InetSocketAddress) channel.receive(buf);
            String fromHost = receive.getAddress().getHostAddress();
            int fromPort = receive.getPort();
            String msg = NatUtil.getMsgByByteBuffer(buf, true);
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Log.e(TAG,"onRead: fromHost: "+fromHost+
                    " fromPort: "+ fromPort +
                    " msg: " + jsonObject.toString());
            postPacketEvent(
                    new PacketEvent.Builder()
                    .fromHost(fromHost)
                    .fromPort(fromPort)
                    .msg(jsonObject)
                    .build()
            );
        }catch (Exception e){

        }
    }
}