package com.example.natclient.fun.impl.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.natclient.bean.NatResponse;
import com.example.natclient.engine.RequestQueue;
import com.example.natclient.fun.base.AbsSelectedHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.fun.base.IRequestObserver;

/**
 * @author YGX
 *
 * 获取客户端
 */

public class GetClientTask extends AbsTask {

    public GetClientTask(AbsSelectedHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        IRequestObserver observer = RequestQueue.get(json.getLongValue("mid"));
        if(observer == null) return;

        int code = json.getIntValue("code");
        NatResponse resp;
        if(code == 200){
            JSONObject result = json.getJSONObject("result");
            JSONArray nats = result.getJSONArray("nats");
            resp = new NatResponse.Builder()
                    .code(200)
                    .success(true)
                    .r(result.toString())
                    .build();
        }else {
            resp = new NatResponse.Builder()
                    .code(code)
                    .success(false)
                    .em(json.getString("em"))
                    .build();
        }

        observer.onResponse(resp);
    }
}
