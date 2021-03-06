package com.example.burrowserver.consumer.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.base.domain.event.PacketEvent;
import com.example.base.key.Key;
import com.example.base.task.abs.AbsUDPTask;
import com.example.burrowserver.engine.repository.NatClientRepository;
import com.example.utils.Log;

import java.nio.channels.DatagramChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * @author YGX
 *
 * <p> nat客户端获取其他在线的客户端标签 </p>
 */
public class GetClientsTask extends AbsUDPTask {
    private static final String TAG = "GetClientsTask";
    public GetClientsTask(DatagramChannel channel) {
        super(channel);
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        JSONObject baseResp = getRespJSONObjectBase(event.msg);
        String paramTag = getParamTag(event.msg);

        if (!NatClientRepository.containsTag(paramTag)) {
            stuffFailureResp(baseResp,
                    Key.Code.NO_LTAG,
                    Key.Em.NO_LTAG);
            return;
        }

        Set<String> activeTags = NatClientRepository.getActiveTags();
        HashSet<String> tags = new HashSet<>(activeTags);
        tags.remove(paramTag);
        JSONObject extra = new JSONObject();
        extra.put("tags",tags);
        // extra.getJSONArray("tags").remove(paramTag);
        baseResp.put("extra",extra);
        Log.e(TAG, "handlePacket: baseResp: "+baseResp);
        stuffAndSendSuccessResp(baseResp,event.fromHost,event.fromPort);
    }

    private String getParamTag(JSONObject object){
        JSONObject params = object.getJSONObject("params");
        return params.getString("tag");
    }
}