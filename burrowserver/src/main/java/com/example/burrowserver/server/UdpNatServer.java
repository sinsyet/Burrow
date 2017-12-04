package com.example.burrowserver.server;




import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.engine.IBurrowObserver;
import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.engine.TaskExecutor;
import com.example.burrowserver.engine.fun.ISelectedHandler;
import com.example.burrowserver.engine.impl.BaseAcceptMsgHandleImpl;
import com.example.burrowserver.engine.impl.BurrowAcceptMsgHandleImpl;
import com.example.burrowserver.engine.impl.SelectedHandlerImpl;
import com.example.burrowserver.eventbus.EventBus;
import com.example.burrowserver.eventbus.anno.Subscribe;
import com.example.burrowserver.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UdpNatServer{
    private static final String TAG = UdpNatServer.class.getSimpleName();
    private DatagramChannel mBaseChannel;
    private DatagramChannel mBurrowChannel;
    private boolean mAcceptFlag;
    private Selector mSelector;

    private ISelectedHandler selectedHandler;

    public UdpNatServer(int port) throws IOException {

        initChannel(port);
        initSelector();

        registerMsgHandler();
        initSelectedHandler();

        EventBus.subscribe(this);
    }

    private void initSelectedHandler(){
        selectedHandler = new SelectedHandlerImpl();
    }

    private void registerMsgHandler(){
        Repository.registerMsgHandler(mBaseChannel,new BaseAcceptMsgHandleImpl(mBaseChannel));
        Repository.registerMsgHandler(mBurrowChannel,new BurrowAcceptMsgHandleImpl(mBurrowChannel));
    }
    private void initSelector() throws IOException {
        mSelector = Selector.open();
        mBaseChannel.register(mSelector, SelectionKey.OP_READ);
        mBurrowChannel.register(mSelector, SelectionKey.OP_READ);
    }

    private void initChannel(int port) throws IOException {
        mBaseChannel = DatagramChannel.open();

        mBaseChannel.socket().bind(new InetSocketAddress(port));
        mBaseChannel.configureBlocking(false);
        mBurrowChannel = DatagramChannel.open();
        mBurrowChannel.configureBlocking(false);
    }

    public void launchServer(){
        if(mAcceptFlag) throw new IllegalStateException("don't launch server repeat");
        TaskExecutor.exec(mAcceptTask);
        Log.e(TAG,"server has launch");
    }

    private Runnable mAcceptTask = new Runnable() {
        @Override
        public void run() {
            mAcceptFlag = true;
            while (mAcceptFlag)
            {
                try {
                    int selectCount = mSelector.select(3 * 1000);
                    if(selectCount < 1) continue;
                    Set<SelectionKey> selectionKeys = mSelector.selectedKeys();
                    for(SelectionKey key : selectionKeys){
                        int ops = key.interestOps();
                        switch (ops){
                            case SelectionKey.OP_READ:
                                selectedHandler.onRead(key);
                                break;
                            default:
                                Log.i(TAG,"Unknow opts");
                                break;
                        }
                    }

                    selectionKeys.clear();
                } catch (IOException e) {
                    Log.e(TAG,"select error",e);
                }
            }
        }
    };

    @Subscribe(BurrowAction.class)
    public void onBurrowAction(BurrowAction burrowAction){
        Log.e(TAG,"onBurrowAction: "+burrowAction.getBurrowToken());
    }

    private Map<String,BurrowAction> mBurrowActions = new HashMap<>();
    public void launchBurrowAction(BurrowAction burrowAction,IBurrowObserver observer){
        burrowAction.setObserver(observer);
        mBurrowActions.put(burrowAction.getBurrowToken(),burrowAction);
        observer.onStart(mBurrowChannel,burrowAction);
    }
}
