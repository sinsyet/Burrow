package com.example.burrowserver.engine.repository.impl.burrow;

import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.engine.repository.base.AbsTask;

import java.nio.channels.DatagramChannel;

/**
 * @author YGX
 * 打洞响应任务
 */
public class BurrowRespTask extends AbsTask {
    public BurrowRespTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void onRunTask(JSONObject obj) {
        String token = obj.getString("token");
        BurrowAction burrowAction = Repository.getBurrowAction(token);
        NatClient local = burrowAction.getLocal();
        String host = local.host;
        int port = local.port;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t",11); // 11 服务器在获取到远程认可后通知主呼的标签
        jsonObject.put("token",token);
        int code = obj.getIntValue("code");
        jsonObject.put("code",code);
        if(code == 200){
            JSONObject extra = obj.getJSONObject("extra");
            JSONObject respExtra = new JSONObject();
            respExtra.put("host",obj.getString("host"));
            respExtra.put("port",extra.getIntValue("port"));
            jsonObject.put("extra",respExtra);
        }else{
            jsonObject.put("em",obj.getString("em"));
            // 去掉打洞
            Repository.releaseBurrowAction(token);
        }
        sendMsg(jsonObject.toString(),host,port);
    }
}