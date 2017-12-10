package com.example.natclient.fun.impl.channelhandler;

import com.example.natclient.fun.base.AbsUDPChannelHandler;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class BurrowHandlerImpl extends AbsUDPChannelHandler {
    public BurrowHandlerImpl(DatagramChannel channel) {
        super(channel);
    }

    @Override
    public void onRead(SelectionKey key) {

    }
}