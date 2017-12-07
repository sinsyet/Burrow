package com.example.burrowserver.engine.impl;


import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.engine.base.AbsAcceptMsgHandler;
import com.example.burrowserver.engine.repository.base.AbsTask;
import com.example.burrowserver.engine.repository.impl.burrow.BurrowPitpatTask;
import com.example.burrowserver.engine.repository.impl.burrow.BurrowRespTask;
import com.example.utils.Log;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ygx
 *
 * 处理打洞消息的消息处理者
 */

public class BurrowAcceptMsgHandleImpl extends AbsAcceptMsgHandler/*implements IAcceptMsgHandler*/ {
    private static final String TAG = "BurrowAcceptMsgHandleImpl";
    public BurrowAcceptMsgHandleImpl(DatagramChannel channel) {
        super(channel);
    }

    private Map<Integer,AbsTask> mTask = new HashMap<>();
    {
        mTask.put(-10,new BurrowRespTask(getChannel()));
        mTask.put(-11,new BurrowPitpatTask(getChannel()));
    }
    @Override
    public void onHandleMsg(InetSocketAddress remote, String msg) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            if (jsonObject.containsKey("t")) {
                int t = jsonObject.getIntValue("t");
                AbsTask absTask = mTask.get(t);
                if(absTask  == null)
                    throw new IllegalArgumentException("No this function with type "+t);

                jsonObject.put("host",remote.getAddress().getHostAddress());
                jsonObject.put("port",remote.getPort());

                absTask.runTask(jsonObject);
            }
        }catch (Exception e){
            Log.e(TAG,"error when handle base msg ",e);
        }
    }
}
