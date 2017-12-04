package com.example.burrowserver.engine.fun;


import java.nio.channels.SelectionKey;

public interface ISelectedHandler {

    void onRead(SelectionKey key);
}
