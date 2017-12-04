package com.example.burrowserver.engine.fun;


import java.net.InetSocketAddress;

public interface IAcceptMsgHandler {

    void onHandleMsg(InetSocketAddress remote, String msg);
}
