package com.example.natclient.fun.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.app.Key;
import com.example.natclient.bean.NatResponse;
import com.example.natclient.engine.RequestQueue;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.fun.base.IRequestObserver;
import com.example.natclient.fun.base.ISelectedHandler;


/**
 *
 * @author YGX
 *
 * 注册任务
 */

public class RegisterTask extends AbsTask {


    public RegisterTask(AbsSelectedHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        long mid = json.getLongValue("mid");
        int code = json.getIntValue("code");
        IRequestObserver observer = RequestQueue.remove(mid);
        NatResponse resp = null;

        if(code == 200){
            JSONObject result = json.getJSONObject("result");
            String tag = result.getString("tag");
            handler.onRegistered(tag,json.getString("host"),json.getIntValue("port"));
            if(observer != null){
                resp = new NatResponse.Builder()
                        .success(true)
                        .r(result.toString())
                        .code(Key.Code.OK)
                        .build();
            }
        }else {
            if(observer != null){
                resp = new NatResponse.Builder()
                        .success(true)
                        .em(json.getString("em"))
                        .code(code)
                        .build();
            }
        }
        if(observer != null){
            observer.onResponse(resp);
        }
    }
}
