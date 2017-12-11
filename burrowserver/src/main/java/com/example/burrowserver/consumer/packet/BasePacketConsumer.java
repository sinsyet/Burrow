package com.example.burrowserver.consumer.packet;

import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;

import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 *
 * <p>基本报文消费者</p>
 */
public class BasePacketConsumer extends AbsPacketConsumer {

    public BasePacketConsumer(DatagramChannel udpChannel) {
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