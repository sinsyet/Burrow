package com.example.natclient.fun.impl.resp;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.utils.Log;

/**
 * @author YGX
 *
 * t为31的响应任务
 */
public class BurrowResp_31Task extends AbsTask {
    private static final String TAG = "BurrowResp_31Task";
    public BurrowResp_31Task(AbsUDPChannelHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        Log.e(TAG,"BurrowResp -31 task: "+json);
    }
}