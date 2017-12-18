package com.example.burrowserver.consumer.packet;

import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.consumer.task.BurrowTask;
import com.example.burrowserver.consumer.task.GetClientsTask;
import com.example.burrowserver.consumer.task.PitpatTask;
import com.example.burrowserver.consumer.task.RegisterTask;
import com.example.burrowserver.consumer.task.Resp_51Task;

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
        mTasks.put(Key.T.REQ_REGISTER,new RegisterTask(udpChannel));
        mTasks.put(Key.T.REQ_PITPAT,new PitpatTask(udpChannel));
        mTasks.put(Key.T.REQ_GET_CLIENTS,new GetClientsTask(udpChannel));
        mTasks.put(Key.T.REQ_BURROW,new BurrowTask(udpChannel));
        mTasks.put(-Key.T.REQ_51,new Resp_51Task(udpChannel));
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