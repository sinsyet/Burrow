package com.example.base.channel.ifun;

import java.nio.channels.SelectionKey;

public interface IChannelHandler {

    void onAccept(SelectionKey key);

    void onConnect(SelectionKey key);

    void onRead(SelectionKey key);

    void onWrite(SelectionKey key);

    void onSelect(SelectionKey key);
}