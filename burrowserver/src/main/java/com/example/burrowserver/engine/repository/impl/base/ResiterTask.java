package com.example.burrowserver.engine.repository.impl.base;

import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.base.Key;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.engine.repository.base.AbsTask;
import com.example.utils.NatUtil;

import java.nio.channels.DatagramChannel;


/**
 *
 * @author ygx
 * 注册
 */

public class ResiterTask extends AbsTask {
    private static final String TAG = "ResiterTask";
    public ResiterTask(DatagramChannel channel){
        super(channel);
    }

    @Override
    protected void onRunTask(JSONObject obj) {
        // 获取主机和端口
        int port = obj.getIntValue("port");
        String host = obj.getString("host");
        String usn = obj.getString("usn");

        // 以主机和端口作为参数,生成用户tag
        String tag = NatUtil.base64Encode(host, port);

        // 创建反馈的json
        JSONObject responseJson = new JSONObject();
        int t = obj.getIntValue("t");
        responseJson.put("t",-t);
        responseJson.put("mid",obj.getLongValue("mid"));
        // 保存到服务端集合中
        if (Repository.containsTag(tag)) {
            responseJson.put("code", Key.Code.REPEAT_REGISTER);
            responseJson.put("em",Key.Msg.REGISTER_REPEAT);
        }else {
            NatClient natClient = new NatClient(host,
                    port,
                    System.currentTimeMillis(),
                    tag,
                    usn,
                    false);
            Repository.cacheNatClient(natClient);
            responseJson.put("code",Key.Code.OK);
            JSONObject result = new JSONObject();
            result.put("tag",tag);
            responseJson.put("result",result);
        }
        sendMsg(responseJson.toString(),host,port);
    }
}
