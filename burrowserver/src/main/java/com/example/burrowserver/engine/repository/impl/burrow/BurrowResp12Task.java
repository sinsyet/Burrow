package com.example.burrowserver.engine.repository.impl.burrow;

import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.engine.repository.base.AbsTask;
import com.example.utils.Log;

import java.nio.channels.DatagramChannel;

public class BurrowResp12Task extends AbsTask {
    private static final String TAG = "BurrowResp12Task";
    public BurrowResp12Task(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void onRunTask(JSONObject obj) {
        Log.e(TAG,"obj: "+obj.toString());
    }
}