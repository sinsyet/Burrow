package com.example.burrowserver.engine.repository.impl.base;

import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.base.Key;
import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.engine.repository.base.AbsTask;

import java.nio.channels.DatagramChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * @author YGX
 * 获取在线设备
 */

public class GetClientsTask extends AbsTask {
    public GetClientsTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void onRunTask(JSONObject obj) {
        JSONObject resp = getResponseJson(obj);

        String tag = obj.getString("tag");
        if (Repository.containsTag(tag)) {
            Set<String> tags = Repository.getTags();
            resp.put("code",200);
            HashSet<String> hashSet = new HashSet<>(tags);
            hashSet.remove(tag);
            JSONObject result = new JSONObject();
            result.put("tags",hashSet);
            resp.put("result",result);
        }else {
            resp.put("code", Key.Code.NO_TAG);
            resp.put("em",Key.Msg.NO_LOCAL_TAG);
        }
        sendMsg(resp.toString());
    }
}
