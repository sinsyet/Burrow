package com.example.natclient.repository;

import com.example.natclient.bean.BurrowEvent;

import java.util.HashMap;
import java.util.Map;

public class Repository {

    private static Map<String,BurrowEvent> sBurrowActions = new HashMap<>();

    public static boolean containBurrowAction(String token){
        return sBurrowActions.containsKey(token);
    }

    public static void put(String key,BurrowEvent burrowAction){
        sBurrowActions.put(key, burrowAction);
    }

    public static BurrowEvent get(String key){
        return sBurrowActions.get(key);
    }
}