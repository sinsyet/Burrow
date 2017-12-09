package com.example.natclient.fun.impl.task;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.utils.Log;

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
