package com.example.base.channel.abs;

import com.example.base.channel.ifun.IChannelHandler;
import com.example.jutil.utils.Log;

import java.nio.channels.SelectionKey;

/**
 * @author YGX
 *
 * <p>TCP channel handler 的基类</p>
 *
 * <p> 预留< /p>
 */
public abstract class AbsTCPChannelHandler implements IChannelHandler {
    private static final String TAG = "AbsTCPChannelHandler";
    @Override
    public void onSelect(SelectionKey key) {
            int ops = key.interestOps();
            switch (ops){
                case SelectionKey.OP_READ:
                    onRead(key);
                    break;
                case SelectionKey.OP_ACCEPT:
                    onAccept(key);
                    break;
                case SelectionKey.OP_CONNECT:
                    onConnect(key);
                    break;
                case SelectionKey.OP_WRITE:
                    onWrite(key);
                    break;
                default:
                    Log.e(TAG,"onSelect: unknown ops: "+ops);
                    break;
            }
    }
}