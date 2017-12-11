package com.example.base.channel.abs;

import com.example.base.channel.ifun.IChannelHandler;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;
import com.example.utils.Log;

import java.nio.channels.SelectionKey;
import java.util.Set;


/**
 * @author YGX
 *
 * <p>UDP channel handler的基类</p>
 *
 * <p>UDP channel只需要处理read operation即可</p>
 */
public abstract class AbsUDPChannelHandlerImpl implements IChannelHandler {
    protected AbsPacketConsumer consumer;
    private static final String TAG = "AbsUDPChannelHandlerImpl";
    public AbsUDPChannelHandlerImpl(AbsPacketConsumer consumer){
        this.consumer = consumer;
    }
    @Override
    public void onAccept(SelectionKey key) {
        // empty operation
    }

    @Override
    public void onConnect(SelectionKey key) {
        // empty operation
    }

    @Override
    public void onWrite(SelectionKey key) {
        // empty operation
    }

    @Override
    public void onSelect(SelectionKey key) {
            int ops = key.interestOps();
            switch (ops){
                case SelectionKey.OP_READ:
                    onRead(key);
                    break;
                default:
                    Log.e(TAG,"onSelect: unknown ops: "+ops);
                    break;
            }
    }

    protected void postPacketEvent(PacketEvent event){
        if(consumer != null){
            consumer.onPacketEvent(event);
        }
    }
}