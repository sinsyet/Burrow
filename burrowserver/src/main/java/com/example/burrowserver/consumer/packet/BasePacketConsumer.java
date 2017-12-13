package com.example.burrowserver.consumer.packet;

import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.consumer.task.GetClientsTask;
import com.example.burrowserver.consumer.task.PitpatTask;
import com.example.burrowserver.consumer.task.RegisterTask;

import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YGX
 *
 * <p>基本报文消费者</p>
 */
public class BasePacketConsumer extends AbsPacketConsumer {
    private Map<Integer,AbsUDPTask> mTasks = new HashMap<>();
    {
        mTasks.put(1,new RegisterTask(udpChannel));
        mTasks.put(2,new PitpatTask(udpChannel));
        mTasks.put(3,new GetClientsTask(udpChannel));
    }
    public BasePacketConsumer(DatagramChannel udpChannel) {
        super(udpChannel);
    }

    @Override
    protected boolean canConsume(PacketEvent event) {
        int t = event.msg.getIntValue("t");
        return mTasks.containsKey(t);
        // return true;
    }

    @Override
    protected void consumePacket(PacketEvent event) {
        int t = event.msg.getIntValue("t");
        AbsUDPTask absUDPTask = mTasks.get(t);
        if (absUDPTask != null) {
            absUDPTask.handle(event);
        }
    }
}