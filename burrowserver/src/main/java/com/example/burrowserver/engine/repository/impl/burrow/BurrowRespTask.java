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
        jsonObject.put("t",12); // 12 服务器在获取到远程认可后通知主呼的标签
        jsonObject.put("token",token);
        JSONObject extra = new JSONObject();
        extra.put("host",obj.getString("host"));
        extra.put("port",obj.getIntValue("port"));
        jsonObject.put("extra",extra);

        sendMsg(jsonObject.toString(),host,port);
    }
}