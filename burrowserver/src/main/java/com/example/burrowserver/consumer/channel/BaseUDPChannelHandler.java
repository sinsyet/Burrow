package com.example.burrowserver.consumer.channel;

import com.example.base.channel.abs.AbsUDPChannelHandlerImpl;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;
import com.example.utils.Log;
import com.example.utils.NatUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class BaseUDPChannelHandler extends AbsUDPChannelHandlerImpl {
    private static final String TAG = "BaseUDPChannelHandler";
    private ByteBuffer buf = ByteBuffer.allocate(1024);
    public BaseUDPChannelHandler(AbsPacketConsumer consumer) {
        super(consumer);
    }

    @Override
    public void onRead(SelectionKey key) {
        synchronized (this) {
            DatagramChannel channel = (DatagramChannel) key.channel();
            try {
                InetSocketAddress receive = (InetSocketAddress) channel.receive(buf);
                String msg = NatUtil.getMsgByByteBuffer(buf, true);
                String host = receive.getAddress().getHostAddress();
                int port = receive.getPort();
                postPacketEvent(
                        new PacketEvent.Builder()
                                .fromHost(host)
                                .fromPort(port)
                                .msg(msg)
                                .build()
                );
            } catch (IOException e) {
                Log.e(TAG,"onRead fail : "+e.getMessage());
            }
        }
    }
}