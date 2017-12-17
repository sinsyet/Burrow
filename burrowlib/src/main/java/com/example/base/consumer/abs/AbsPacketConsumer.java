package com.example.base.consumer.abs;


import com.example.base.event.PacketEvent;

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

    public AbsPacketConsumer setNextPacketConsumer(AbsPacketConsumer next){
        if(next == null)
            throw new IllegalStateException(
                    "next packet consumer can't be null");
        if(this.mNext == null){
            this.mNext = next;
        }else{
            this.mNext.setNextPacketConsumer(next);
        }
        return this;
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

    public DatagramChannel getChannel(){
        return udpChannel;
    }
    protected abstract boolean canConsume(PacketEvent event);
    protected abstract void consumePacket(PacketEvent event);
}