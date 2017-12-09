package com.example.natclient.fun.impl.net;

import com.example.natclient.fun.base.AbsSelectedHandler;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class BurrowHandlerImpl extends AbsSelectedHandler {
    public BurrowHandlerImpl(DatagramChannel channel) {
        super(channel);
    }

    @Override
    public void onRead(SelectionKey key) {

    }
}