package com.example.burrowserver.server;

import com.example.base.channel.ifun.IChannelHandler;
import com.example.base.consumer.abs.AbsPacketConsumer;
import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.consumer.channel.BaseUDPChannelHandler;
import com.example.burrowserver.consumer.packet.BasePacketConsumer;
import com.example.burrowserver.consumer.packet.BurrowPacketConsumer;
import com.example.burrowserver.engine.repository.BurrowActionRepository;
import com.example.engine.TaskExecutors;
import com.example.eventbus.EventBus;
import com.example.eventbus.anno.Subscribe;
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


/**
 * @author YGX
 *
 * <p> nat服务器 </p>
 */
public class NatServer {
    private static final String TAG = "NatServer";
    private final int port;
    private Observer observer;

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
                    int select = selector.select(3 * 1000);
                    if (select == 0) {
                        continue;
                    }
                    Set<SelectionKey> keys = selector.selectedKeys();
                    // notices, 不要用selector.keys(); 来接收key
                    for(SelectionKey key:keys){
                        IChannelHandler iChannelHandler = mChannelHandlerMap.get(getChannelKey(key.channel()));
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

    public boolean sendMsgByChannel1(String host,int port,String msg){
        try {
            sendMsg(mBaseChannel,msg,host,port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendMsgByBurrowChannel(String host,int port,String msg){
        try {
            sendMsg(mBurrowChannel,msg,host,port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Class<? extends SelectableChannel> getChannelKey(SelectableChannel channel){
        if(channel instanceof DatagramChannel) return DatagramChannel.class;
        if(channel instanceof SocketChannel) return SocketChannel.class;
        if(channel instanceof ServerSocketChannel) return ServerSocketChannel.class;

        return null;
    }
    private void sendMsg(DatagramChannel channel,String msg,String host,int port) throws IOException {
        channel.send(ByteBuffer.wrap(msg.getBytes("UTF-8")),new InetSocketAddress(host,port));
    }

    public void launch() throws IOException {
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
            EventBus.subscribe(this);
        }catch (Exception e){
            // NatUtil.close(mBaseChannel,mBurrowChannel);
            throw e;
        }
    }

    private void initChannelHandler() {
        mChannelHandlerMap.put(DatagramChannel.class,
                new BaseUDPChannelHandler(packetConsumer));
    }

    private void registerPacketHandler() {
        packetConsumer = new BasePacketConsumer(mBaseChannel);
        /*packetConsumer.setNextPacketConsumer(
                new BurrowPacketConsumer(mBurrowChannel));*/
    }

    private void initSelector() throws IOException {
        selector = Selector.open();
        mBaseChannel.register(selector, SelectionKey.OP_READ);
        mBurrowChannel.register(selector, SelectionKey.OP_READ);
    }

    private void initChannel() throws IOException {

        mBaseChannel = DatagramChannel.open();
        mBaseChannel.socket().bind(new InetSocketAddress(port));
        Log.e(TAG,"initChannel: "+port+", ");
        mBaseChannel.configureBlocking(false);
        mBurrowChannel = DatagramChannel.open();
        mBurrowChannel.configureBlocking(false);
    }

    @Subscribe(String.class)
    void onBurrowAction(String token){
        BurrowAction burrowAction = BurrowActionRepository.getBurrowAction(token);
        if(burrowAction == null) return;
        switch (burrowAction.getActionStep()){
            case BurrowAction.Step.CREATE:
                launchBurrowAction(burrowAction);
                break;
            case BurrowAction.Step.RESP:
                respBurrowAction(burrowAction);
                break;
        }
    }

    private void respBurrowAction(BurrowAction action){
        try {
            NatClient local = action.getLocal();
            String respMsg = (String) local.getTag();
            /*mBurrowChannel*/mBaseChannel.send(
                    ByteBuffer.wrap(respMsg.getBytes("UTF-8")),
                    new InetSocketAddress(local.host,local.burrowPort));
        }catch (Exception e){
            Log.e(TAG,"respBurrowAction: "+e.getMessage());
        }
    }
    private void launchBurrowAction(BurrowAction burrowAction) {
        try {
            burrowAction.launch(/*mBurrowChannel*/mBaseChannel);
        } catch (IOException e) {

        }
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