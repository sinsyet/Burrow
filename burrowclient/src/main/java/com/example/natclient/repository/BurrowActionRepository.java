package com.example.natclient.repository;

import com.example.natclient.bean.ClientBurrowAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YGX
 *
 * <p> 打洞事件的仓库 </p>
 */
public class BurrowActionRepository {

    private static Map<String,ClientBurrowAction> sBurrowEvents =
            new HashMap<>();

    public static void put(String key,ClientBurrowAction event){
        sBurrowEvents.put(key,event);
    }

    public static boolean containsToken(String token){
        return sBurrowEvents.containsKey(token);
    }

    public static ClientBurrowAction getBurrowEvent(String token){
        if(token == null) return null;
        return sBurrowEvents.get(token);
    }
}