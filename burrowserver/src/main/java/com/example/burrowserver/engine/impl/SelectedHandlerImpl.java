package com.example.burrowserver.engine.impl;

import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.engine.fun.IAcceptMsgHandler;
import com.example.burrowserver.engine.fun.ISelectedHandler;
import com.example.burrowserver.utils.NatUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class SelectedHandlerImpl implements ISelectedHandler {
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    @Override
    public void onRead(SelectionKey key) {
        DatagramChannel channel = (DatagramChannel) key.channel();
        try {
            InetSocketAddress remote = (InetSocketAddress) channel.receive(byteBuffer);
            IAcceptMsgHandler handler = Repository.getMsgHandler(channel);
            String msg = NatUtil.getMsgByByteBuffer(byteBuffer, true);
            handler.onHandleMsg(remote,msg);
        } catch (IOException ignored) {

        }
    }
}
