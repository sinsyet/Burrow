package com.example.natclient.fun.base;

import com.example.natclient.bean.BurrowEvent;

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

    public void onBurrow(BurrowEvent action){
        if(dispatchBurrowAction(action)){
            handleBurrow(action);
        }else {
            if (this.mNextHandler != null) {
                this.mNextHandler.onBurrow(action);
            }
        }
    }

    protected abstract void handleBurrow(BurrowEvent action);
    protected abstract boolean dispatchBurrowAction(BurrowEvent action);

}