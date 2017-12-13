package com.example.natclient.fun.impl.task;

import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.task.abs.AbsUDPTask;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.utils.Log;

import java.nio.channels.DatagramChannel;

/**
 *
 * @author YGX
 *
 * 心跳
 */

public class PitpatTask extends AbsUDPTask {
    private static final String TAG = "PitpatTask";

    public PitpatTask(DatagramChannel handler) {
        super(handler);
    }

    protected void onRunTask(JSONObject json) {

    }

    @Override
    protected void handlePacket(PacketEvent event) {
        onRunTask(event.msg);
    }
}
