package com.example.natclient.fun.base;

import java.nio.channels.DatagramChannel;

/**
 *
 * @author YGX
 */

public abstract class AbsSelectedHandler implements ISelectedHandler,IHandleObserver {

    private DatagramChannel channel;
    private IHandleObserver observer;

    public AbsSelectedHandler(DatagramChannel channel){
        this.channel = channel;
    }

    protected DatagramChannel getChannel() {
        return channel;
    }

    @Override
    public void setHandleObserver(IHandleObserver observer){
        this.observer = observer;
    }

    @Override
    public void onPitpat(String tag,String host,int port){
        if (observer != null) {
            observer.onPitpat(tag, host, port);
        }
    }

    @Override
    public void onRegistered(String tag, String host, int port) {
        if (observer != null) {
            observer.onRegistered(tag, host, port);
        }
    }
}
