package com.example.burrowserver.consumer.task;

import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.utils.Log;

import java.nio.channels.DatagramChannel;

public class Resp_4Task extends AbsUDPTask {
    private static final String TAG = "Resp_4Task";
    public Resp_4Task(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        Log.e(TAG, "handlePacket: "+event.msg);
    }
}