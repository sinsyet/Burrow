package com.example.burrowserver.engine.repository;



import com.example.burrowserver.bean.BurrowAction;
import com.example.burrowserver.bean.NatClient;

import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Repository {
    private static Map<String,NatClient> sNatClients = new HashMap<>();

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


    public static void put(String key,BurrowAction action){
        BurrowAction burrowAction = sBurrowActionMaps.put(key, action);
        // 销毁
    }

    public static BurrowAction getBurrowAction(String key)
    {
        return sBurrowActionMaps.get(key);
    }

    public static void releaseBurrowAction(String token){
        sBurrowActionMaps.remove(token);
    }
}
