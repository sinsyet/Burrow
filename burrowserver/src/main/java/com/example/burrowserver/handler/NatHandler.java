package com.example.burrowserver.handler;



import com.example.burrowserver.base.Key;
import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.BurrowObserverImpl2;
import com.example.burrowserver.engine.repository.Repository;
import com.example.burrowserver.server.UdpNatServer;
import com.example.burrowserver.utils.NatUtil;

import java.net.InetSocketAddress;
import java.util.Set;

public class NatHandler {

    /*public static String handleResister(String msg, InetSocketAddress remote) {
        String[] register = msg.split("#");
        StringBuilder sBuf = new StringBuilder();
        sBuf.append("-1");
        sBuf.append("#").append(register[1]);

        int port = remote.getPort();
        String hostName = remote.getHostName();
        String tag = NatUtil.generateTag(hostName, String.valueOf(port));
        NatClient natClient = Repository.getNatClientByTag(tag);
        if(natClient == null){
            natClient = new NatClient(hostName,
                    port,
                    System.currentTimeMillis(),
                    tag,
                    false);
            Repository.cacheNatClient(natClient);
        }else{
            natClient.activeStamp = System.currentTimeMillis();
        }
        sBuf.append("#")
                .append(tag);
        return sBuf.toString();
    }
*/
    public static String handlePitpat(String msg){
        StringBuilder sBuf = new StringBuilder();
        String[] pitpat = msg.split("#");
        sBuf.append("-2").append("#");

        NatClient natClient = Repository.getNatClientByTag(pitpat[1]);
        if(natClient == null){
            sBuf.append(Key.Code.NO_TAG).append("NO TAG");
        }else {
            natClient.activeStamp = System.currentTimeMillis();
            sBuf.append(Key.Code.OK).append("OK");
        }

        return sBuf.toString();
    }

    public static String handleGetNatClient(String msg){
        StringBuilder sBuf = new StringBuilder();
        String[] getNatClient = msg.split("#");
        String remoteTag = getNatClient[2];
        sBuf.append("-3")
                .append("#")
                .append(getNatClient[1])
                .append("#");

        if(Repository.containsTag(remoteTag)){
            Set<String> tags = Repository.getTags();
            tags.remove(remoteTag);
            int index = 0;
            sBuf.append(Key.Code.OK).append("#");
            for(String s : tags){
                if(index > 0){
                    sBuf.append("@");
                }
                sBuf.append(s);
                index++;
            }
        }else{
            sBuf.append(Key.Code.NO_TAG).append("INVALID TAG");
        }
        return sBuf.toString();
    }

    public static String handleBurrow(String msg, UdpNatServer server){
        String[] burrow = msg.split("#");
        //4#时间戳#自身别名#远程别名

        // 检查别名是否合法
        String callingParty = burrow[2];
        String calledParty = burrow[3];
        StringBuilder sBuf = new StringBuilder();
        sBuf.append("-4").append("#").append(burrow[1]).append("#");
        if(!Repository.containsTag(callingParty)){

            sBuf.append(Key.Code.NO_TAG)
                    .append("#")
                    .append(Key.Msg.NO_LOCAL_TAG);
        }else if(!Repository.containsTag(calledParty)){
            sBuf.append(Key.Code.NO_TAG)
                    .append("#")
                    .append(Key.Msg.NO_REMOTE_TAG);
        }else {
            NatClient calledNat = Repository.getNatClientByTag(calledParty);
            if (calledNat.isNating) {
                sBuf.append(Key.Code.REMOTE_IS_NATING)
                        .append("#")
                        .append(Key.Msg.REMOTE_IS_NATING);
            }else {
                BurrowAction burrowAction = new BurrowAction(
                        Repository.getNatClientByTag(callingParty),
                        Repository.getNatClientByTag(calledParty));
                sBuf.append(Key.Code.OK)
                        .append("#")
                        .append(calledNat.host)
                        .append("@").append(calledNat.port)
                        .append("@").append(burrowAction.getBurrowToken());
                server.launchBurrowAction(burrowAction,new BurrowObserverImpl2());
            }
        }

        return sBuf.toString();
    }
}
