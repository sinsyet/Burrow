package com.example.natclient.fun.impl.task;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.utils.Log;

public class SayHiRespTask extends AbsTask {
    private static final String TAG = "SayHiRespTask";
    public SayHiRespTask(AbsUDPChannelHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        Log.e(TAG,"onRunTask: "+json.toString());
    }
}