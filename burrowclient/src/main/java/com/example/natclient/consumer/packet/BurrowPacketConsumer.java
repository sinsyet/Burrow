package com.example.natclient.consumer.packet;

import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.natclient.fun.impl.task.BurrowTask;
import com.example.natclient.fun.impl.task.GetClientTask;
import com.example.natclient.fun.impl.task.PitpatTask;
import com.example.natclient.fun.impl.task.RegisterTask;

import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

public class BurrowPacketConsumer extends AbsPacketConsumer {
    private Map<Integer,AbsUDPTask> mTasks = new HashMap<>();
    {
        mTasks.put(Key.T.REQ_51,new BurrowTask(getChannel()));
    }
    public BurrowPacketConsumer(DatagramChannel udpChannel) {
        super(udpChannel);
    }

    @Override
    protected boolean canConsume(PacketEvent event) {
        return mTasks.containsKey(event.t);
    }

    @Override
    protected void consumePacket(PacketEvent event) {
        AbsUDPTask absUDPTask = mTasks.get(event.t);
        if(absUDPTask != null){
            absUDPTask.handle(event);
        }
    }
}