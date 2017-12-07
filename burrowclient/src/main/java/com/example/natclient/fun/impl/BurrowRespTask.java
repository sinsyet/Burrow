package com.example.natclient.fun.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.engine.Handler;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.utils.Log;

import java.util.TimerTask;

public class BurrowRespTask extends AbsTask {
    private static final String TAG = "BurrowRespTask";
    public BurrowRespTask(AbsSelectedHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        Log.e(TAG,"onRunTask: "+json);
        JSONObject extra = json.getJSONObject("extra");

        Handler.post(new TimerTask() {
            @Override
            public void run() {
                JSONObject sayHi = new JSONObject();
                sayHi.put("t",21);
                sayHi.put("mid",System.currentTimeMillis());
                JSONObject extra2 = new JSONObject();
                extra2.put("msg","Hi beijiao");
                sayHi.put("extra",extra2);
                sendMsg(sayHi.toString(),extra.getString("host"),extra.getIntValue("port"));
            }
        },600);
    }
}