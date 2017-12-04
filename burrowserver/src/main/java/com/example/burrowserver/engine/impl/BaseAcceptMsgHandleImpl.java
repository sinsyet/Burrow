package com.example.burrowserver.engine.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.burrowserver.engine.base.AbsAcceptMsgHandler;
import com.example.burrowserver.engine.repository.base.AbsTask;
import com.example.burrowserver.engine.repository.impl.base.BurrowTask;
import com.example.burrowserver.engine.repository.impl.base.GetClientsTask;
import com.example.burrowserver.engine.repository.impl.base.PitpatTask;
import com.example.burrowserver.engine.repository.impl.base.ResiterTask;
import com.example.burrowserver.utils.Log;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

/**
 *
 * @author ygx
 *
 * 处理基本接收信息的处理者
 */

public class BaseAcceptMsgHandleImpl extends AbsAcceptMsgHandler/* implements IAcceptMsgHandler*/ {

    private static final String TAG = BaseAcceptMsgHandleImpl.class.getSimpleName();
    private HashMap<Integer,AbsTask> sTask = new HashMap<>();
    {
        sTask.put(1,new ResiterTask(getChannel()));
        sTask.put(2,new PitpatTask(getChannel()));
        sTask.put(3,new GetClientsTask(getChannel()));
        sTask.put(4,new BurrowTask(getChannel()));
    }

    public BaseAcceptMsgHandleImpl(DatagramChannel channel) {
        super(channel);
    }

    @Override
    public void onHandleMsg(InetSocketAddress remote, String msg) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            if (jsonObject.containsKey("t")) {
                int t = jsonObject.getIntValue("t");
                AbsTask absTask = sTask.get(t);
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
