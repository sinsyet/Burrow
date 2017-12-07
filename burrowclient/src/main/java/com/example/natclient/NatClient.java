package com.example.natclient;

import com.alibaba.fastjson.JSONObject;
import com.example.engine.TaskExecutors;
import com.example.eventbus.EventBus;
import com.example.eventbus.anno.Subscribe;
import com.example.natclient.app.Key;
import com.example.natclient.bean.Message;
import com.example.natclient.bean.NatResponse;
import com.example.natclient.engine.RequestQueue;
import com.example.natclient.fun.base.IHandleObserver;
import com.example.natclient.fun.base.IRequestObserver;
import com.example.natclient.fun.base.ISelectedHandler;
import com.example.natclient.fun.impl.BaseHandlerImpl;
import com.example.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author YGX
 *
 * 客户端
 */

public class NatClient implements IHandleObserver {
    private static final String TAG = "NatClient";
    private ISelectedHandler handler;
    private Selector selector;
    private DatagramChannel channel;

    private String msg;
    private String host;
    private int port;
    private Timer mTimer = new Timer();
    private TimerTask mPitpatTask = new TimerTask() {
        @Override
        public void run() {
            try {
                JSONObject pitpat = new JSONObject();
                pitpat.put("t",2);
                // pitpat.put("mid",System.currentTimeMillis());
                pitpat.put("tag",tag);
                msg = pitpat.toString();
                sendMsg(msg,host,port);
            } catch (Exception e) {
                Log.e(TAG,"error when sendMsg in timer",e);
            }
        }
    };
    private String tag;


    public NatClient() throws IOException {
        channel = DatagramChannel.open();
        selector = Selector.open();
        handler = new BaseHandlerImpl(channel);
        channel.configureBlocking(false);
        handler.setHandleObserver(this);
        channel.register(selector, SelectionKey.OP_READ);
        EventBus.subscribe(this);
    }

    private boolean mLaunchFlag = false;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            mLaunchFlag = true;
            while (mLaunchFlag){
                try {
                    int select = selector.select(2 * 1000);
                    if(select < 1) continue;

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for(SelectionKey key:selectionKeys){
                        int i = key.interestOps();
                        switch (i)
                        {
                            case SelectionKey.OP_READ:
                                handler.onRead(key);
                                break;

                            default:
                                    Log.e(TAG,"Unknow key: "+ i);
                                    break;
                        }
                    }
                    selectionKeys.clear();

                } catch (IOException e) {
                    Log.e(TAG,"error when select ",e);
                }
            }
        }
    };

    public void launch() {
        TaskExecutors.exec(r);
    }

    public void registerServer(String usn, String host, int port, IRequestObserver observer){
        JSONObject obj = new JSONObject();
        obj.put("t",1);
        long mid = System.currentTimeMillis();
        obj.put("mid", mid);
        obj.put("usn",usn);

        try {
            sendMsg(obj.toString(),host,port);
            if (observer != null) {
                RequestQueue.put(mid, observer);
            }
        } catch (Exception e) {
            if (observer != null) {
                observer.onResponse(new NatResponse.Builder()
                        .code(Key.Code.CLOSED_CHANNEL)
                        .success(false)
                        .t(e)
                        .build());
            }
        }
    }

    private void sendMsg(String msg,String host,int port) throws Exception {
        channel.send(ByteBuffer.wrap(msg.getBytes("UTF-8")),new InetSocketAddress(host,port));
    }

    @Override
    public void onPitpat(String tag, String host, int port) {

    }

    @Override
    public void onRegistered(String tag, String host, int port) {
        Log.i(TAG,"onRegistered: tag:"+tag+" host: "+host+" port:"+port);
        this.tag = tag;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t",2);
        jsonObject.put("mid",System.currentTimeMillis());
        jsonObject.put("tag", tag);

        this.msg = jsonObject.toString();
        this.host = host;
        this.port = port;
        // 第一次是10秒后发起心跳, 然后每隔60秒发一次心跳
        mTimer.schedule(mPitpatTask,10 * 1000, 60 * 1000);
    }

    public void getClient(IRequestObserver observer) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t",3);
        long mid = System.currentTimeMillis();
        jsonObject.put("mid", mid);
        jsonObject.put("tag",tag);
        try {
            sendMsg(jsonObject.toString(),host,port);
            RequestQueue.put(mid,observer);
        } catch (Exception e) {
            if (observer != null) {
                observer.onResponse(new NatResponse.Builder()
                        .success(false)
                        .em(e.getMessage())
                        .t(e)
                        .code(Key.Code.CLOSED_CHANNEL)
                        .build());
            }
        }
    }

    public void burrow(String target,IRequestObserver observer) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t",4);
        long mid = System.currentTimeMillis();
        jsonObject.put("mid", mid);
        jsonObject.put("ltag",tag);
        jsonObject.put("rtag",target);
        try {
            sendMsg(jsonObject.toString(),host,port);
            RequestQueue.put(mid,observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(Message.class)
    public void sendMsg(Message msg){
        try {
            channel.send(
                    ByteBuffer.wrap(msg.msg.getBytes("UTF-8")),
                    new InetSocketAddress(msg.host,msg.port));
        } catch (IOException e) {

        }
    }
}
