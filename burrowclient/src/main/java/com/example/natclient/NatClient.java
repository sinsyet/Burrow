package com.example.natclient;

import com.alibaba.fastjson.JSONObject;
import com.example.base.channel.ifun.IChannelHandler;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.engine.TaskExecutors;
import com.example.eventbus.EventBus;
import com.example.eventbus.anno.Subscribe;
import com.example.natclient.app.Key;
import com.example.natclient.bean.ClientBurrowAction;
import com.example.natclient.bean.Message;
import com.example.natclient.bean.NatResponse;
import com.example.natclient.consumer.channel.BaseUDPChannelHandlerImpl;
import com.example.natclient.consumer.packet.BasePacketConsumer;
import com.example.natclient.consumer.packet.BurrowPacketConsumer;
import com.example.natclient.consumer.packet.BurrowReqPacketConsumer;
import com.example.natclient.engine.RequestQueue;
import com.example.natclient.fun.base.IRequestObserver;
import com.example.natclient.fun.impl.channelhandler.InitiativeBurrowHandlerImpl;
import com.example.natclient.repository.BurrowActionRepository;
import com.example.natclient.repository.ChannelRepository;
import com.example.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author YGX
 *
 * <p> nat 客户端 </p>
 */
public class NatClient {
    private static final String TAG = "NatClient";
    private static final int BURROW_PORT = 26566;
    private DatagramChannel mClientBaseChannel;
    private Selector selector;

    private Map<Integer,AbsPacketConsumer> mBurrowConsumers
            = new HashMap<>();

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
                pitpat.put("tag",tag);
                msg = pitpat.toString();
                sendMsg(msg,host,port);
            } catch (Exception e) {
                Log.e(TAG,"error when sendMsg in timer",e);
            }
        }
    };
    private String tag;
    private DatagramChannel mClientBurrowChannel;
    private int mBurrowPort;
    private BasePacketConsumer basePacketConsumer;
    private IChannelHandler baseUDPChannelHandler;
    private Map<Class<? extends SelectableChannel>,IChannelHandler> mChannelHandlers
            = new HashMap<>();
    public NatClient() throws IOException {
        mClientBurrowChannel = DatagramChannel.open();
        mBurrowPort = mClientBurrowChannel.socket().getPort();
        mClientBurrowChannel.configureBlocking(false);

        mClientBaseChannel = DatagramChannel.open();
        selector = Selector.open();
        basePacketConsumer = new BasePacketConsumer(mClientBaseChannel);
        basePacketConsumer.setNextPacketConsumer(
                new BurrowPacketConsumer(mClientBurrowChannel));
        baseUDPChannelHandler = new BaseUDPChannelHandlerImpl(basePacketConsumer);
        mClientBaseChannel.configureBlocking(false);
        mClientBaseChannel.register(selector, SelectionKey.OP_READ);
        mClientBurrowChannel.register(selector, SelectionKey.OP_READ);
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
                                SelectableChannel channel = key.channel();
                                if (channel instanceof DatagramChannel) {
                                    baseUDPChannelHandler.onSelect(key);
                                }else if(channel instanceof SocketChannel){
                                    //
                                }else if(channel instanceof ServerSocketChannel){
                                    //
                                }
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
        // obj.put("usn",usn);

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
    private Class<? extends SelectableChannel> getChannelKey(SelectableChannel channel){
        if(channel instanceof DatagramChannel) return DatagramChannel.class;
        if(channel instanceof SocketChannel) return SocketChannel.class;
        if(channel instanceof ServerSocketChannel) return ServerSocketChannel.class;

        return null;
    }
    private void sendMsg(String msg,String host,int port) throws Exception {
        mClientBaseChannel.send(ByteBuffer.wrap(msg.getBytes("UTF-8")),
                new InetSocketAddress(host,port));
    }

    @Subscribe(JSONObject.class)
    void onRegistered(JSONObject obj) {
        Log.e(TAG, "onRegistered: "+obj.toString());
        this.tag = obj.getString("tag");
        this.host = obj.getString("host");
        this.port = obj.getIntValue("port");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t",2);
        jsonObject.put("mid",System.currentTimeMillis());
        jsonObject.put("tag", tag);
        this.msg = jsonObject.toString();
        // 第一次是10秒后发起心跳, 然后每隔60秒发一次心跳
        mTimer.schedule(mPitpatTask,10 * 1000, 60 * 1000);
    }

    @Subscribe(String.class)
    void onClientBurrowAction(String token){
        ClientBurrowAction burrowEvent = BurrowActionRepository.getBurrowEvent(token);
        if(burrowEvent == null) return;

        String respMsg = (String) burrowEvent.getTag();
        try {
            mClientBurrowChannel.send(
                    ByteBuffer.wrap(respMsg.getBytes("UTF-8")),
                    new InetSocketAddress(burrowEvent.serverHost,burrowEvent.serverPort));

            // 向被叫发送报文
            JSONObject sayHi = new JSONObject();
            sayHi.put("t",-101);
            mClientBurrowChannel.send(
                    ByteBuffer.wrap(sayHi.toString().getBytes("UTF-8")),
                    new InetSocketAddress(burrowEvent.host,burrowEvent.port)
            );
        } catch (IOException ignored) {

        }
    }

    public void getClient(IRequestObserver observer) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t",3);
        long mid = System.currentTimeMillis();
        jsonObject.put("mid", mid);
        JSONObject params = new JSONObject();
        params.put("tag",tag);
        jsonObject.put("params",params);
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
        JSONObject params = new JSONObject();
        params.put("tag",tag);
        params.put("rtag",target);
        jsonObject.put("params",params);
        try {
            DatagramChannel burrowChannel = getBurrowChannel();
            int port = burrowChannel.socket().getPort();
            // burrowChannel.configureBlocking(false);
            AbsPacketConsumer burrowReqPacketConsumer =
                    new BurrowReqPacketConsumer(burrowChannel);
            basePacketConsumer.setNextPacketConsumer(burrowReqPacketConsumer);
            // burrowChannel.register(selector,SelectionKey.OP_READ);
            mBurrowConsumers.put(port,burrowReqPacketConsumer);
            sendMsg(burrowChannel,jsonObject.toString(),host, this.port);
            RequestQueue.put(mid,observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(DatagramChannel channel,String msg,String host,int port) {
        try {
            channel.send(ByteBuffer.wrap(msg.getBytes("utf-8")),new InetSocketAddress(host,port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DatagramChannel getBurrowChannel() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.register(selector,SelectionKey.OP_READ);
        // ChannelRepository.register(datagramChannel,new InitiativeBurrowHandlerImpl(datagramChannel));
        return datagramChannel;
    }

    @Subscribe(Message.class)
    public void sendMsg(Message msg){
        try {
            mClientBaseChannel.send(
                    ByteBuffer.wrap(msg.msg.getBytes("UTF-8")),
                    new InetSocketAddress(msg.host,msg.port));
        } catch (IOException e) {

        }
    }
}
