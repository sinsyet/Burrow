package com.example.natclient.fun.impl.resp;

import com.alibaba.fastjson.JSONObject;
import com.example.engine.Handler;
import com.example.natclient.app.Key;
import com.example.natclient.bean.BurrowEvent;
import com.example.natclient.fun.base.AbsTask;
import com.example.natclient.fun.base.AbsUDPChannelHandler;
import com.example.natclient.repository.Repository;

import java.util.TimerTask;

/**
 * @author YGX
 *
 * response type -4
 *
 */
public class BurrowResp_4Task extends AbsTask {

    public BurrowResp_4Task(AbsUDPChannelHandler handler) {
        super(handler);
    }

    @Override
    protected void onRunTask(JSONObject json) {
        JSONObject respJson = getRespJson(json);
        String token = json.getString("token");
        BurrowEvent burrowAction = Repository.get(token);
        if (burrowAction == null) {
            burrowAction = new BurrowEvent();
            JSONObject extra = json.getJSONObject("extra");
            burrowAction.host = extra.getString("host");
            burrowAction.port = extra.getIntValue("port");
            burrowAction.token = token;
            Repository.put(token,burrowAction);
        }
        respJson.put("code", Key.Code.OK);
        respJson.put("token",token);
        sendMsg(respJson.toString(),json.getString("host"),json.getIntValue("port"));
        final BurrowEvent action = burrowAction;
        Handler.post(new TimerTask() {
            @Override
            public void run() {
                JSONObject sayHi = new JSONObject();
                sayHi.put("t",22);
                sayHi.put("mid",System.currentTimeMillis());
                JSONObject extra2 = new JSONObject();
                extra2.put("msg","Hi, zhujiao");
                sayHi.put("extra",extra2);
                sendMsg(sayHi.toString(),action.host,action.port);
            }
        },500);
    }
}