package com.example.burrowserver.consumer.channel.base;

import com.example.base.channel.ifun.IChannelHandler;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.burrowserver.consumer.channel.BaseUDPChannelHandler;

import java.nio.channels.SelectionKey;

public class BaseChannelHandler implements IChannelHandler {
    public BaseChannelHandler(AbsPacketConsumer consumer){}
    @Override
    public void onAccept(SelectionKey key) {

    }

    @Override
    public void onConnect(SelectionKey key) {

    }

    @Override
    public void onRead(SelectionKey key) {

    }

    @Override
    public void onWrite(SelectionKey key) {

    }
}