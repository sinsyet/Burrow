package com.example.burrowserver.engine.repository.impl.base;

import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.base.Key;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.engine.repository.base.AbsTask;

import java.nio.channels.DatagramChannel;

/**
 * @author ygx
 *
 * 心跳
 */

public class PitpatTask extends AbsTask {


    public PitpatTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void onRunTask(JSONObject obj) {
        String tag = obj.getString("tag");
        String host = obj.getString("host");
        int port = obj.getIntValue("port");
        NatClient natClient = Repository.getNatClientByTag(tag);
        JSONObject resp = getResponseJson(obj);
        if(natClient == null){
            // no this client;
            resp.put("code", Key.Code.NO_TAG);
            resp.put("em",Key.Msg.NO_LOCAL_TAG);
        }else{
            // update client active stamp;
            natClient.activeStamp = System.currentTimeMillis();
            resp.put("code",Key.Code.OK);
        }

        sendMsg(resp.toString(),host,port);
    }
}
