package com.example.burrowserver.server;

import com.example.base.channel.ifun.IChannelHandler;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.burrowserver.consumer.channel.BaseUDPChannelHandler;
import com.example.burrowserver.consumer.packet.BasePacketConsumer;
import com.example.engine.TaskExecutors;
import com.example.eventbus.EventBus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author YGX
 *
 * <p> nat服务器 </p>
 */
public class NatServer {
    private final int port;
    private final Observer observer;

    private DatagramChannel mBaseChannel;
    private DatagramChannel mBurrowChannel;
    private Selector selector;
    private AbsPacketConsumer packetConsumer;
    private Map<Class<? extends SelectableChannel>,IChannelHandler> mChannelHandlerMap =
            new HashMap<>();

    private boolean mAcceptFlag = false;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            mAcceptFlag = true;
            while (mAcceptFlag){
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                    Set<SelectionKey> keys = selector.keys();

                    for(SelectionKey key:keys){
                        Class<? extends SelectableChannel> channelClz = key.channel().getClass();
                        IChannelHandler iChannelHandler = mChannelHandlerMap.get(channelClz);
                        if(iChannelHandler != null){
                            iChannelHandler.onSelect(key);
                        }
                    }
                    keys.clear();
                } catch (IOException e) {

                }
            }
        }
    };

    public NatServer(int port,Observer observer){
        this.port = port;
        this.observer = observer;
    }

    public void launch(){
        if (observer != null) {
            observer.onLaunchStart();
        }
        try {
            initChannel();
            launchProgress(Progress.CHANNEL_CREATED);
            initSelector();
            launchProgress(Progress.SELECTOR_REGISTER);
            registerPacketHandler();
            initChannelHandler();
            launchProgress(Progress.CHANNEL_HANDLER_CREATED);
            TaskExecutors.exec(r);
            launchProgress(Progress.SERVER_LAUNCH);
            if(observer != null){
                observer.onLaunchComplete(true);
            }
        }catch (Exception e){
            launchFail(e);
            return;
        }
        EventBus.subscribe(this);
    }

    private void initChannelHandler() {
        mChannelHandlerMap.put(DatagramChannel.class,
                new BaseUDPChannelHandler(packetConsumer));
    }

    private void registerPacketHandler() {
        packetConsumer = new BasePacketConsumer(mBaseChannel);
        packetConsumer.setAndReturnNextPacketConsumer(
                new BasePacketConsumer(mBurrowChannel));
    }

    private void initSelector() throws IOException {
        selector = Selector.open();
        mBaseChannel.register(selector, SelectionKey.OP_READ);
        mBurrowChannel.register(selector, SelectionKey.OP_READ);
    }

    private void initChannel() throws IOException {

        mBaseChannel = DatagramChannel.open();
        mBaseChannel.socket().bind(new InetSocketAddress(port));
        mBaseChannel.configureBlocking(false);
        mBurrowChannel = DatagramChannel.open();
        mBurrowChannel.configureBlocking(false);
    }

    private void launchProgress(int progress){
        if(observer != null){
            observer.onLaunchProgress(progress);
        }
    }

    private void launchFail(Throwable t){
        if(observer != null){
            observer.onLaunchFail(t);
        }
    }
    public interface Observer{
        void onLaunchStart();

        void onLaunchProgress(int progress);

        void onLaunchComplete(boolean success);

        void onLaunchFail(Throwable t);
    }

    public interface Progress{
        int CHANNEL_CREATED         = 1;
        int SELECTOR_REGISTER       = 2;
        int CHANNEL_HANDLER_CREATED = 3;
        int SERVER_LAUNCH           = 4;
    }
    private Map<Integer,String> mProgressMap =
            new HashMap<>();
    {
        mProgressMap.put(Progress.CHANNEL_CREATED,"channel created");
        mProgressMap.put(Progress.SELECTOR_REGISTER,"selector registered");
        mProgressMap.put(Progress.CHANNEL_HANDLER_CREATED,"channel handler create");
        mProgressMap.put(Progress.SERVER_LAUNCH,"server launched");
    }
}