package com.example.natclient.engine;

import com.example.natclient.fun.base.IRequestObserver;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author YGX
 *
 * 请求池
 */

public class RequestQueue {
    private static Map<Long,IRequestObserver> mRequestQueue = new HashMap<>();
    public static void put(long mid,IRequestObserver observer){
        mRequestQueue.put(mid,observer);
    }

    public static IRequestObserver get(long mid){
        return mRequestQueue.get(mid);
    }

    public static IRequestObserver remove(long mid){
        return mRequestQueue.remove(mid);
    }
}
