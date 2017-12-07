package com.example.natclient.repository;

import com.example.natclient.bean.BurrowAction;

import java.util.HashMap;
import java.util.Map;

public class Repository {

    private static Map<String,BurrowAction> sBurrowActions = new HashMap<>();

    public static boolean containBurrowAction(String token){
        return sBurrowActions.containsKey(token);
    }

    public static void put(String key,BurrowAction burrowAction){
        sBurrowActions.put(key, burrowAction);
    }

    public static BurrowAction get(String key){
        return sBurrowActions.get(key);
    }
}