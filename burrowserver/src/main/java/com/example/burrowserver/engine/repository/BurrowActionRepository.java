package com.example.burrowserver.engine.repository;

import com.example.burrowserver.bean.BurrowAction;

import java.util.HashMap;
import java.util.Map;

public class BurrowActionRepository {
    private static Map<String,BurrowAction> sBurrowActions =
            new HashMap<>();

    public static void put(String token,BurrowAction action){
        sBurrowActions.put(token, action);
    }

    public static BurrowAction getBurrowAction(String token){
        return sBurrowActions.get(token);
    }

    public static boolean containsToken(String token){
        return sBurrowActions.containsKey(token);
    }
}