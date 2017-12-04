package com.example.burrowserver.engine.repository.impl.base;

import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.base.Key;
import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.engine.repository.base.AbsTask;
import com.example.burrowserver.eventbus.EventBus;

import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 *
 * 发起打洞
 */

public class BurrowTask extends AbsTask {

     public BurrowTask(DatagramChannel channel) {
        super(channel);
    }
    /*
    {
        "t":4,
        "mid":8877,
        "ltag":"localtag",
        "rtag":"remote tag"
    }
    // 反馈
    {
        "t":-4,
        "mid":8877,
        "code":200,
        "em":"cuowuxinxi",
        "result":{
            "host":"www.baidu.com",
            "port":12345
        }
    }
     */
    @Override
    protected void onRunTask(JSONObject obj) {
        JSONObject resp = getResponseJson(obj);
        // local tag
        String ltag = obj.getString("ltag");
        // remote tag
        String rtag = obj.getString("rtag");
        if(!Repository.containsTag(ltag)){
            resp.put("code", Key.Code.NO_TAG);
            resp.put("em",Key.Msg.NO_LOCAL_TAG);
            sendMsg(resp.toString());
            return;
        }

        if(!Repository.containsTag(rtag)){
            resp.put("code", Key.Code.NO_REMOTE_TAG);
            resp.put("em",Key.Msg.NO_REMOTE_TAG);
            sendMsg(resp.toString());
            return;
        }

        NatClient natClient = Repository.getNatClientByTag(rtag);
        if(natClient.isNating){
            resp.put("code", Key.Code.REMOTE_IS_NATING);
            resp.put("em",Key.Msg.REMOTE_IS_NATING);
            sendMsg(resp.toString());
            return;
        }

        BurrowAction burrowAction = new BurrowAction(Repository.getNatClientByTag(ltag), natClient);
        resp.put("code",Key.Code.OK);
        JSONObject result = new JSONObject();
        result.put("token",burrowAction.getBurrowToken());
        resp.put("result",result);
        sendMsg(resp.toString());
        EventBus.post(burrowAction);
    }
}
