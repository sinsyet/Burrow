package com.example.natclient.fun.impl.req;

import com.alibaba.fastjson.JSONObject;
import com.example.natclient.app.Key;
import com.example.natclient.bean.ClientBurrowAction;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.repository.Repository;

public class BurrowReq31Task extends AbsTask {

    public BurrowReq31Task(AbsUDPChannelHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        JSONObject respJson = getRespJson(json);
        String token = json.getString("token");
        if(Repository.containBurrowAction(token)){
            respJson.put("token", token);
            ClientBurrowAction burrowEvent = Repository.get(token);
            burrowEvent.updateActiveStamp();
        }else {
            respJson.put("code", Key.Code.INVALID_TOKEN);
            respJson.put("em",Key.Msg.INVALID_TOKEN);
        }
        sendMsg(respJson.toString(),json.getString("host"),json.getIntValue("port"));
    }
}