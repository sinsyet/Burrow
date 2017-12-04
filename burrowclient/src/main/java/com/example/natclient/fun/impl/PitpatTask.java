package com.example.natclient.fun.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.fun.base.ISelectedHandler;
import com.example.natclient.utils.Log;

import java.nio.channels.DatagramChannel;

/**
 *
 * @author YGX
 *
 * 心跳
 */

public class PitpatTask extends AbsTask {
    private static final String TAG = "PitpatTask";

    public PitpatTask(AbsSelectedHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        Log.e(TAG,"onRunTask: "+json.toJSONString());
        // handler.onPitpat();
    }
}
