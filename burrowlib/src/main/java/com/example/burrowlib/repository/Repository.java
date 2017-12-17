package com.example.burrowlib.repository;


import com.example.burrowlib.bean.ClientBurrowAction;

import java.util.HashMap;
import java.util.Map;

public class Repository {

    private static Map<String,ClientBurrowAction> sBurrowActions = new HashMap<>();

    public static boolean containBurrowAction(String token){
        return sBurrowActions.containsKey(token);
    }

    public static void put(String key,ClientBurrowAction burrowAction){
        sBurrowActions.put(key, burrowAction);
    }

    public static ClientBurrowAction get(String key){
        return sBurrowActions.get(key);
    }
}