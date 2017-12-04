package com.example.burrowserver.bean;


import com.example.burrowserver.engine.IBurrowObserver;
import com.example.burrowserver.server.UdpNatServer;
import com.example.burrowserver.utils.NatUtil;

public class BurrowAction {
    private NatClient local;
    private NatClient remote;
    private IBurrowObserver observer;

    public BurrowAction(NatClient local, NatClient remote){
        this.local = local;
        this.remote = remote;
    }

    public void setObserver(IBurrowObserver observer){
        this.observer = observer;
    }

    /**
     * 生成打洞token
     * @return 打洞token
     */
    public String getBurrowToken(){
        String localTag = NatUtil.generateTag(local.host, local.port);
        String remoteTag = NatUtil.generateTag(remote.host, remote.port);
        return NatUtil.generateTag(localTag,remoteTag);
    }

    public void launch(UdpNatServer engine){

    }
}
