package com.example.burrowserver.consumer.packet;

import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;

import java.nio.channels.DatagramChannel;

public class BurrowPacketConsumer extends AbsPacketConsumer {
    public BurrowPacketConsumer(DatagramChannel udpChannel) {
        super(udpChannel);
    }

    @Override
    protected boolean canConsume(PacketEvent event) {
        return true;
    }

    @Override
    protected void consumePacket(PacketEvent event) {

    }
}