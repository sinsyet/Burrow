package com.example.natclient.fun.base;

import com.example.natclient.bean.ClientBurrowAction;

/**
 * @author YGX
 *
 * 打洞处理者基类
 */
public abstract class AbsBurrowHandler {
    // 下一个处理者
    private AbsBurrowHandler mNextHandler;

    public AbsBurrowHandler nextHandler(AbsBurrowHandler handler){
        this.mNextHandler = handler;
        return this;
    }

    public AbsBurrowHandler getNextHandler(){
        return this.mNextHandler;
    }

    public void onBurrow(ClientBurrowAction action){
        if(dispatchBurrowAction(action)){
            handleBurrow(action);
        }else {
            if (this.mNextHandler != null) {
                this.mNextHandler.onBurrow(action);
            }
        }
    }

    protected abstract void handleBurrow(ClientBurrowAction action);
    protected abstract boolean dispatchBurrowAction(ClientBurrowAction action);

}