package com.example.natclient.fun.base;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.engine.TaskExecutors;

/**
 *
 * @author YGX
 *
 * 任务
 */

public abstract class AbsTask {

    protected AbsSelectedHandler handler;
    private JSONObject json;

    public AbsTask(AbsSelectedHandler handler){
        this.handler = handler;
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            onRunTask(json);
        }
    };

    public void runTask(JSONObject json){
        this.json = json;
        TaskExecutors.exec(r);
    }

    protected abstract void onRunTask(JSONObject json);
}
