package com.example.burrowserver.engine;

import com.example.burrowserver.bean.BurrowAction;

import java.nio.channels.DatagramChannel;

public interface IBurrowObserver{

    void onStart(DatagramChannel channel, BurrowAction ba);
    void onProgress(DatagramChannel channel, BurrowAction ba, int progress);
    void onFinish(DatagramChannel channel, BurrowAction ba);
}