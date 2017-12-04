package com.example.burrowserver.engine;

import com.example.burrowserver.bean.BurrowAction;

import java.nio.channels.DatagramChannel;

/**
 * Created by YGX on 2017/12/2.
 */

public class BurrowObserverImpl2 implements IBurrowObserver {
    @Override
    public void onStart(DatagramChannel channel, BurrowAction ba) {

    }

    @Override
    public void onProgress(DatagramChannel channel, BurrowAction ba, int progress) {

    }

    @Override
    public void onFinish(DatagramChannel channel, BurrowAction ba) {

    }
}
