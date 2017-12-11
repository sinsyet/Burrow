package com.example.base.consumer.abs;

import com.example.base.domain.event.PacketEvent;

import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 *
 * <p>报文消费者的基类</p>
 */
public abstract class AbsPacketConsumer {
    protected DatagramChannel udpChannel;
    private AbsPacketConsumer mNext;
    public AbsPacketConsumer(DatagramChannel udpChannel){
        this.udpChannel = udpChannel;
    }

    public AbsPacketConsumer setAndReturnNextPacketConsumer(AbsPacketConsumer next){
        if(next == null)
            throw new IllegalStateException(
                    "next packer consumer can't be null");
        this.mNext = next;
        return next;
    }

    public AbsPacketConsumer deletePacketConsumer(AbsPacketConsumer deleteConsumer){
        if (this == deleteConsumer) {
            return mNext;
        }
        if(mNext != null) {
            this.mNext = this.mNext.deletePacketConsumer(deleteConsumer);
        }
        return this;
    }

    public void onPacketEvent(PacketEvent event){
        if(canConsume(event)){
            consumePacket(event);
        }else{
            if(mNext != null){
                mNext.onPacketEvent(event);
            }
        }
    }

    protected abstract boolean canConsume(PacketEvent event);
    protected abstract void consumePacket(PacketEvent event);
}