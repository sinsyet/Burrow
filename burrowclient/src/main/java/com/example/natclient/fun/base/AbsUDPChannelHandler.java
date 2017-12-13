package com.example.natclient.fun.base;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

/**
 * @author YGX
 *
 * use {@link com.example.base.channel.abs.AbsUDPChannelHandler} instead;
 */
@Deprecated
public abstract class AbsUDPChannelHandler implements IChannelHandler {

    private DatagramChannel channel;

    public AbsUDPChannelHandler(DatagramChannel channel){
        this.channel = channel;
    }

    protected DatagramChannel getChannel() {
        return channel;
    }

    @Override
    public void onAccept(SelectionKey key) {
        // udp channel no supprot acctpe operation
    }

    @Override
    public void onWrite(SelectionKey key) {
        // udp channel no support write operation
    }

    @Override
    public void onConnect(SelectionKey key) {
        // udp channel no support connect operation
    }
}
