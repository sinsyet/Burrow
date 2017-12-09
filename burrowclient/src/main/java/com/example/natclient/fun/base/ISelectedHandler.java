package com.example.natclient.fun.base;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author YGX
 *
 * 处理{@link Selector#select()}结果的接口
 */

public interface ISelectedHandler {

    void onRead(SelectionKey key);

    void onAccept(SelectionKey key);

    void onWrite(SelectionKey key);

    void onConnect(SelectionKey key);

    void setHandleObserver(IHandleObserver observer);


}
