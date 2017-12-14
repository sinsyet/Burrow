package com.example.burrowserver.consumer.packet;

import com.alibaba.fastjson.JSONObject;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.consumer.task.BurrowTask;
import com.example.burrowserver.consumer.task.GetClientsTask;
import com.example.burrowserver.consumer.task.PitpatTask;
import com.example.burrowserver.consumer.task.RegisterTask;
import com.example.burrowserver.consumer.task.Resp_51Task;
import com.example.burrowserver.engine.repository.BurrowActionRepository;

import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YGX
 *
 * <p> 打洞报文消费者 </p>
 */
public class BurrowPacketConsumer extends AbsPacketConsumer {
    private Map<Integer,AbsUDPTask> mTasks = new HashMap<>();
    {
        mTasks.put(-Key.T.REQ_51,new Resp_51Task(udpChannel));
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
        if(absUDPTask!=null){
            absUDPTask.handle(event);
        }
    }
}