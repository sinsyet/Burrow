package com.example.burrowserver.engine.repository;

import com.example.burrowserver.bean.NatClient;
import com.example.utils.Log;
import com.example.utils.NatUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NatClientRepository {

    private static Map<String,NatClient> sClientMap
            = new HashMap<>();

    public static void put(String key,NatClient client){
        if(key == null || client == null) return;
        sClientMap.put(key, client);
    }

    public static NatClient getNatClient(String tag){
        return sClientMap.get(tag);
    }

    public static boolean containsTag(String tag){
        if(tag == null) return false;
        return sClientMap.containsKey(tag);
    }

    public static void updateActiveStamp(String tag){
        NatClient natClient = sClientMap.get(tag);
        if(natClient != null){
            natClient.updateActiveStamp();
        }
    }

    public static Set<String> getActiveTags(){
        return sClientMap.keySet();
    }

    public static void traverseNatClients(){
        for(Map.Entry<String,NatClient> entry: sClientMap.entrySet()){
            Log.i("preview clients",entry.getValue().toString());
        }
    }
}