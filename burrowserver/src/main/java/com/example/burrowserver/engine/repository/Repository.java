package com.example.burrowserver.engine.repository;



import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;
import com.example.burrowserver.engine.fun.IAcceptMsgHandler;

import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Repository {
    private static Map<String,NatClient> sNatClients = new HashMap<>();
    private static Map<DatagramChannel,IAcceptMsgHandler> sChannel2MsgHandler
            = new HashMap<>();
    private static Map<String,BurrowAction> sBurrowActionMaps = new HashMap<>();

    public static boolean containsTag(String tag){
        return sNatClients.containsKey(tag);
    }

    public static NatClient getNatClientByTag(String tag){
        return sNatClients.get(tag);
    }

    public static void cacheNatClient(NatClient natClient) {
        sNatClients.put(natClient.tag,natClient);
    }

    public static Set<String> getTags(){
        return sNatClients.keySet();
    }

    /*public static String[] getUsns(){
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, NatClient> entry : sNatClients.entrySet()) {
            String usn = entry.getValue().usn;
        }
    }*/

    public static void registerMsgHandler(DatagramChannel channel,IAcceptMsgHandler handler){
        sChannel2MsgHandler.put(channel,handler);
    }
    public static IAcceptMsgHandler getMsgHandler(DatagramChannel channel){
        return sChannel2MsgHandler.get(channel);
    }

    public static void put(String key,BurrowAction action){
        BurrowAction burrowAction = sBurrowActionMaps.put(key, action);
        // 销毁
    }

    public static BurrowAction getBurrowAction(String key)
    {
        return sBurrowActionMaps.get(key);
    }
}
