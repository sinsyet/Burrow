package com.example.burrowlib.consumer.packet;

import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowlib.consumer.task.GetClientTask;
import com.example.burrowlib.consumer.task.PitpatTask;
import com.example.burrowlib.consumer.task.RegisterTask;

import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YGX
 *
 * <p> 基本报文消费者 </p>
 */
public class BasePacketConsumer extends AbsPacketConsumer {
    private Map<Integer,AbsUDPTask> mTasks = new HashMap<>();
    {
        mTasks.put(-1,new RegisterTask(getChannel()));
        mTasks.put(-2,new PitpatTask(getChannel()));
        mTasks.put(-3,new GetClientTask(getChannel()));
    }
    public BasePacketConsumer(DatagramChannel udpChannel) {
        super(udpChannel);
    }

    @Override
    protected boolean canConsume(PacketEvent event) {
        int t = event.t;
        return mTasks.containsKey(t);
    }

    @Override
    protected void consumePacket(PacketEvent event) {
        AbsUDPTask absUDPTask = mTasks.get(event.t);
        if(absUDPTask != null){
            absUDPTask.handle(event);
        }
    }
}