package com.example.natclient.fun.impl.channelhandler;

import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.fun.impl.resp.BurrowResp_4Task;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YGX
 *
 * 处理主动发起打洞的handler
 */
public class InitiativeBurrowHandlerImpl extends AbsUDPChannelHandler {
    private Map<Integer,AbsTask> mTasks = new HashMap<>();
    {
        mTasks.put(-4,new BurrowResp_4Task(this));
    }
    public InitiativeBurrowHandlerImpl(DatagramChannel channel) {
        super(channel);
    }

    @Override
    public void onRead(SelectionKey key) {

    }
}