package com.example.burrowlib.consumer.packet;

import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowlib.consumer.task.Resp52Task;
import com.example.burrowlib.consumer.task.Resp_102Task;
import com.example.burrowlib.consumer.task.Resp_4Task;


import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

public class BurrowReqPacketConsumer extends AbsPacketConsumer {
    private static final String TAG = "BurrowReqPacketConsumer";
    private Map<Integer,AbsUDPTask> mTasks = new HashMap<>();
    {
        mTasks.put(Key.T.REQ_52,new Resp52Task(getChannel()));
        mTasks.put(- Key.T.REQ_102,new Resp_102Task(getChannel()));
        mTasks.put(- Key.T.REQ_BURROW,new Resp_4Task(getChannel()));
    }
    public BurrowReqPacketConsumer(DatagramChannel udpChannel) {
        super(udpChannel);
    }

    @Override
    protected boolean canConsume(PacketEvent event) {
        return mTasks.containsKey(event.t);
    }

    @Override
    protected void consumePacket(PacketEvent event) {
        AbsUDPTask absUDPTask = mTasks.get(event.t);
        if (absUDPTask != null) {
            absUDPTask.handle(event);
        }
    }
}