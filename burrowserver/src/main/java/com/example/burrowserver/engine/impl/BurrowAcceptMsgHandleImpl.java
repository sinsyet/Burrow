package com.example.burrowserver.engine.impl;


import com.example.burrowserver.engine.base.AbsAcceptMsgHandler;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * @author ygx
 *
 * 处理打洞消息的消息处理者
 */

public class BurrowAcceptMsgHandleImpl extends AbsAcceptMsgHandler/*implements IAcceptMsgHandler*/ {
    public BurrowAcceptMsgHandleImpl(DatagramChannel channel) {
        super(channel);
    }

    @Override
    public void onHandleMsg(InetSocketAddress remote, String msg) {

    }
}
